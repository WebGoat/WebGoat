resource "oci_identity_compartment" "tf-compartment" {
  compartment_id = var.tenancy_id
  description    = "Compartment for Terraform resources."
  name           = "third-compartment"
  enable_delete  = true
  freeform_tags = {
    git_commit           = "5406d83ae1a7c43b5f1f44a0d044f4622f4a815d"
    git_file             = "terraform/oracle/compartment.tf"
    git_last_modified_at = "2022-04-06 15:27:36"
    git_last_modified_by = "james.woolfenden@gmail.com"
    git_modifiers        = "james.woolfenden"
    git_org              = "bridgecrewio"
    git_repo             = "terragoat"
    yor_trace            = "20f6ad13-d679-4c7a-8d1b-befdd7f16b97"
  }
}