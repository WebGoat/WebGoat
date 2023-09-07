<?php

function bad1() {
    $user_input = $_REQUEST["cmd"];

    // ruleid: tainted-code-execution
    eval($user_input);

    $entityId = $_GET["name"];

    // ruleid: tainted-code-execution
    assert("is_string(" . $entityId. ")");

    // ruleid: tainted-code-execution
    preg_replace('/.*/e',$_POST['code']);

    // ruleid: tainted-code-execution
    $func = new ReflectionFunction($_GET['check']);
    $var = "intermediary";
    $func->invoke("args");

    $a = $_GET['func'];
    // ruleid: tainted-code-execution
    $b = create_function(" ",$a);
    $b();
}

function ok1() {
    // ok: tainted-code-execution
    eval('echo "OK"');

    $env_var = $_ENV["cmd"];

    // ok: tainted-code-execution
    eval($env_var);

    $var = "not_user_input";
    // ok: tainted-code-execution
    $func = new ReflectionFunction($var);
    $var = "intermediary";
    $func->invoke("args");

    $interface = $_GET['iface'];

    if (isset($interface)) {
        // fetch dnsmasq.conf settings for interface
        // ok: tainted-code-execution
        exec('cat '. escapeshellarg(RASPI_DNSMASQ_PREFIX.$interface.'.conf'), $return);
        $conf = ParseConfig($return);
    }

}

?>
