workflow "gitleaks my commits" {
  on = "push"
  resolves = ["gitleaks"]
}

action "gitleaks" {
  uses = "eshork/gitleaks-action@master"
}
