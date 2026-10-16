/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal CBOR (RFC 8949) codec covering the subset used by WebAuthn: unsigned/negative integers,
 * byte strings, text strings, arrays, maps and the simple values false/true/null. Integers are
 * decoded as {@link Long}, byte strings as {@code byte[]}.
 *
 * <p>This exists so the lesson can show every verification step. A production relying party
 * should use a vetted WebAuthn library instead of hand-written parsing.
 */
public final class Cbor {

  private static final int MAX_DEPTH = 16;

  private Cbor() {}

  public static Object decode(byte[] data) {
    Decoder decoder = new Decoder(data, 0);
    Object value = decoder.read(0);
    if (decoder.position != data.length) {
      throw new IllegalArgumentException("Trailing bytes after CBOR item");
    }
    return value;
  }

  /** Decodes the first CBOR item starting at {@code offset}; returns the item and its length. */
  public static Item decodeFirst(byte[] data, int offset) {
    Decoder decoder = new Decoder(data, offset);
    Object value = decoder.read(0);
    return new Item(value, decoder.position - offset);
  }

  public record Item(Object value, int length) {}

  public static byte[] encode(Object value) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    write(out, value);
    return out.toByteArray();
  }

  private static void write(ByteArrayOutputStream out, Object value) {
    switch (value) {
      case null -> out.write(0xf6);
      case Boolean b -> out.write(b ? 0xf5 : 0xf4);
      case Integer i -> write(out, i.longValue());
      case Long l -> {
        if (l >= 0) {
          writeHead(out, 0, l);
        } else {
          writeHead(out, 1, -1 - l);
        }
      }
      case byte[] bytes -> {
        writeHead(out, 2, bytes.length);
        out.writeBytes(bytes);
      }
      case String s -> {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeHead(out, 3, bytes.length);
        out.writeBytes(bytes);
      }
      case List<?> list -> {
        writeHead(out, 4, list.size());
        list.forEach(item -> write(out, item));
      }
      case Map<?, ?> map -> {
        writeHead(out, 5, map.size());
        map.forEach(
            (k, v) -> {
              write(out, k);
              write(out, v);
            });
      }
      default ->
          throw new IllegalArgumentException("Unsupported CBOR type: " + value.getClass());
    }
  }

  private static void writeHead(ByteArrayOutputStream out, int majorType, long length) {
    int major = majorType << 5;
    if (length < 24) {
      out.write(major | (int) length);
    } else if (length < 0x100) {
      out.write(major | 24);
      out.write((int) length);
    } else if (length < 0x10000) {
      out.write(major | 25);
      writeBigEndian(out, length, 2);
    } else if (length < 0x100000000L) {
      out.write(major | 26);
      writeBigEndian(out, length, 4);
    } else {
      out.write(major | 27);
      writeBigEndian(out, length, 8);
    }
  }

  private static void writeBigEndian(ByteArrayOutputStream out, long value, int bytes) {
    for (int i = bytes - 1; i >= 0; i--) {
      out.write((int) (value >>> (8 * i)) & 0xff);
    }
  }

  private static final class Decoder {
    private final byte[] data;
    private int position;

    Decoder(byte[] data, int position) {
      this.data = data;
      this.position = position;
    }

    Object read(int depth) {
      if (depth > MAX_DEPTH) {
        throw new IllegalArgumentException("CBOR nesting too deep");
      }
      int initial = nextByte();
      int majorType = initial >>> 5;
      int additional = initial & 0x1f;
      if (majorType == 7) {
        return switch (additional) {
          case 20 -> Boolean.FALSE;
          case 21 -> Boolean.TRUE;
          case 22 -> null;
          default -> throw new IllegalArgumentException("Unsupported CBOR simple value");
        };
      }
      long argument = readArgument(additional);
      return switch (majorType) {
        case 0 -> argument;
        case 1 -> -1 - argument;
        case 2 -> readBytes(argument);
        case 3 -> new String(readBytes(argument), StandardCharsets.UTF_8);
        case 4 -> {
          long size = checkedLength(argument);
          List<Object> list = new ArrayList<>();
          for (long i = 0; i < size; i++) {
            list.add(read(depth + 1));
          }
          yield list;
        }
        case 5 -> {
          long size = checkedLength(argument);
          Map<Object, Object> map = new LinkedHashMap<>();
          for (long i = 0; i < size; i++) {
            Object key = read(depth + 1);
            if (map.containsKey(key)) {
              throw new IllegalArgumentException("Duplicate CBOR map key: " + key);
            }
            map.put(key, read(depth + 1));
          }
          yield map;
        }
        default -> throw new IllegalArgumentException("Unsupported CBOR major type " + majorType);
      };
    }

    private long readArgument(int additional) {
      if (additional < 24) {
        return additional;
      }
      int length =
          switch (additional) {
            case 24 -> 1;
            case 25 -> 2;
            case 26 -> 4;
            case 27 -> 8;
            default -> throw new IllegalArgumentException("Indefinite lengths are not supported");
          };
      long value = 0;
      for (int i = 0; i < length; i++) {
        value = (value << 8) | nextByte();
      }
      if (value < 0) {
        throw new IllegalArgumentException("CBOR argument too large");
      }
      return value;
    }

    private long checkedLength(long length) {
      // every element needs at least one byte, so this bounds allocations by the input size
      if (length > data.length - position) {
        throw new IllegalArgumentException("CBOR length exceeds input");
      }
      return length;
    }

    private byte[] readBytes(long length) {
      int len = (int) checkedLength(length);
      byte[] result = new byte[len];
      System.arraycopy(data, position, result, 0, len);
      position += len;
      return result;
    }

    private int nextByte() {
      if (position >= data.length) {
        throw new IllegalArgumentException("Unexpected end of CBOR input");
      }
      return data[position++] & 0xff;
    }
  }
}
