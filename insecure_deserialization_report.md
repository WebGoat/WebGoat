# Αναφορά Ευπάθειας: Insecure Deserialization

## 🔍 Τύπος Ευπάθειας
**Ανασφαλής Αποσειριοποίηση Δεδομένων (Insecure Deserialization)**

## 📁 Αρχείο και Γραμμή
`src/main/java/org/owasp/webgoat/plugin/serialization/InsecureDeserializationTask.java:45`

## 📝 Περιγραφή
Ο κώδικας πραγματοποιεί αποσειριοποίηση (deserialization) αντικειμένων που προέρχονται από δεδομένα που παρέχει ο χρήστης. 
Αυτό αποτελεί σοβαρό κίνδυνο ασφαλείας, καθώς ένας κακόβουλος χρήστης μπορεί να δημιουργήσει αντικείμενα με κακόβουλο κώδικα, οδηγώντας σε απομακρυσμένη εκτέλεση κώδικα (Remote Code Execution).

## 💻 Απόσπασμα Κώδικα (Ενδεικτικά)
```java
ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
Object obj = ois.readObject();


⚠️ Γιατί είναι Επικίνδυνο
Ο επιτιθέμενος μπορεί να στείλει τροποποιημένα serialized αντικείμενα ώστε να εκτελέσει αυθαίρετο κώδικα στο σύστημα.

Ειδικά αν η εφαρμογή έχει εξαρτήσεις από κλάσεις που έχουν γνωστά payloads (π.χ. CommonsCollections), ο κίνδυνος είναι πολύ υψηλός.

🎯 Επιπτώσεις
Απομακρυσμένη εκτέλεση κώδικα (RCE)

Διαρροή δεδομένων

Κατάρρευση της εφαρμογής

🛠️ Πρόταση Διόρθωσης
Απόφυγε τη χρήση ObjectInputStream ή παρόμοιων μηχανισμών για δεδομένα που παρέχει ο χρήστης.

Αν απαιτείται serializing, χρησιμοποίησε ασφαλείς εναλλακτικές όπως JSON με manual parsing και validation.

Εάν χρησιμοποιείται Java deserialization, περιόρισε τις αποδεκτές κλάσεις με custom ObjectInputFilter.



 Παράδειγμα Ασφαλέστερης

 Χρήση βιβλιοθήκης JSON όπως Gson ή Jackson για ελεγχόμενο parsing:

Gson gson = new Gson();
UserData data = gson.fromJson(userInput, UserData.class);
