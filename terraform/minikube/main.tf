resource "kubernetes_namespace" "webgoat" {
    metadata {
        name = "webgoat"
    }
}
