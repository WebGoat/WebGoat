# Αναφορά Ευπάθειας: SSRF (Server-Side Request Forgery)

## 🔍 Τύπος Ευπάθειας
**Server-Side Request Forgery (SSRF)**

## 📁 Αρχείο και Γραμμή
`src/main/java/org/owasp/webgoat/plugin/ssrf/SSRFTASK2.java:36`

## 📝 Περιγραφή
Η εφαρμογή επιτρέπει στον χρήστη να ορίσει μια διεύθυνση URL, την οποία ο server χρησιμοποιεί για να πραγματοποιήσει αιτήματα HTTP. Αν και υπάρχει ένας έλεγχος για το `http://ifconfig.pro`, ο έλεγχος είναι επιφανειακός και υπάρχει κίνδυνος να παρακαμφθεί.

## 💻 Απόσπασμα Κώδικα
```java
protected AttackResult furBall(String url) {
    if (url.matches("http://ifconfig\\.pro")) {
        String html;
        try (InputStream in = new URL(url).openStream()) {
            html = new String(in.readAllBytes(), StandardCharsets.UTF_8)
                   .replaceAll("\n", "<br>");
            return success(this).feedback("ssrf.success").output(html).build();
        } catch (IOException e) {
            return failed(this).feedback("ssrf.failure").build();
        }
    }
    return failed(this).feedback("ssrf.failure").build();
}




⚠️ Γιατί είναι Επικίνδυνο
Η χρήση της URL.openStream() με τιμή που ελέγχεται από τον χρήστη μπορεί να επιτρέψει στον επιτιθέμενο να κάνει αιτήματα σε εσωτερικά δίκτυα ή υπηρεσίες.

Επιθέσεις όπως ανακατευθύνσεις, DNS rebinding ή πρόσβαση σε υπηρεσίες cloud metadata είναι πιθανές.

🎯 Επιπτώσεις
Πρόσβαση σε εσωτερικά συστήματα (intranet)

Παραβίαση δεδομένων ή πληροφοριών

Παρακάμψη firewall

🛠️ Πρόταση Διόρθωσης
Μην επιτρέπεις ποτέ στον χρήστη να καθορίζει απευθείας URL.

Αν είναι απαραίτητο, επίτρεψε μόνο επιλογή από ασφαλή whitelist.

Έλεγξε με ασφάλεια ότι το domain και IP είναι εντός επιτρεπόμενων ορίων.

Απέφυγε την αυτόματη ανακατεύθυνση και απαγόρευσε αιτήματα σε μη εξωτερικές διευθύνσεις.

Παράδειγμα Διόρθωσης:

if (!url.equals("http://ifconfig.pro")) {
    return failed(this).feedback("ssrf.failure").build();
}
