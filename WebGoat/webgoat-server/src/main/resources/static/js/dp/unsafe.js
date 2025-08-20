
// DP smoke vuln (JS)

const input = location.hash.replace('#','') || "alert('x')";
eval(input); // intentionally unsafe

