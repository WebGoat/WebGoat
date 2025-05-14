Περιγραφή Ευπάθειας

Η ευπάθεια "Zip Slip" επιτρέπει σε επιτιθέμενο να δημιουργήσει αρχεία zip που περιέχουν μονοπάτια τύπου ../, τα οποία αποσυμπιέζονται έξω από τον επιθυμητό κατάλογο.


Επιπτώσεις
Υπεργραφή ευαίσθητων αρχείων (π.χ. αρχεία συστήματος)

Εκτέλεση κακόβουλου κώδικα

Παραβίαση της ασφάλειας του συστήματος αρχείων

Διόρθωση

File destFile = new File(destDir, entry.getName());
String destPath = destFile.getCanonicalPath();
if (!destPath.startsWith(destDir.getCanonicalPath())) {
    throw new IOException("Attempt to unzip outside target directory");
}

