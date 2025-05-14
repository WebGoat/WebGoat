Η ευπάθεια "XML External Entity (XXE)" προκύπτει όταν μία εφαρμογή επεξεργάζεται XML δεδομένα που προέρχονται από μη έμπιστες πηγές,χωρίς να απενεργοποιεί την επίλυση εξωτερικών οντοτήτων.

Επιπτώσεις
Ανάγνωση ευαίσθητων αρχείων από το σύστημα (π.χ. /etc/passwd)

Server-Side Request Forgery (SSRF)

Αρνήσεις υπηρεσίας (DoS)


Βήματα για Διόρθωση

Απενεργοποίούμε την επίλυση εξωτερικών οντοτήτων στον XML parser:

SAXParserFactory factory = SAXParserFactory.newInstance();
factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
