data "oci_objectstorage_namespace" "example" {
  compartment_id = oci_identity_compartment.tf-compartment.id
}
