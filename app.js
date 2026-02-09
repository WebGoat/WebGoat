// CWE-798: Use of Hard-coded Credentials
// This code demonstrates the vulnerability of hard-coding credentials in source code

const http = require('http');
const crypto = require('crypto');

// VULNERABILITY: Hard-coded database credentials
const DB_USERNAME = 'admin';
const DB_PASSWORD = 'SuperSecret123!';
const DB_HOST = 'localhost';
const DB_NAME = 'production_db';

// VULNERABILITY: Hard-coded API key
const API_KEY = 'sk_live_51Hq3kL9mN0pQrStUvWxYzAbCdEfGhIjKlMnOpQrStUvWxYz';

// VULNERABILITY: Hard-coded authentication token
const AUTH_TOKEN = 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';

// VULNERABILITY: Hard-coded encryption key
const ENCRYPTION_KEY = 'mySecretKey12345678';

// Function that uses hard-coded credentials
function connectToDatabase() {
    const connectionString = `mongodb://${DB_USERNAME}:${DB_PASSWORD}@${DB_HOST}/${DB_NAME}`;
    console.log('Connecting to database...');
    // In real code, this would actually connect to the database
    return { connected: true, connectionString: connectionString };
}

// Function that makes API call with hard-coded API key
function makeAPICall(endpoint) {
    const options = {
        hostname: 'api.example.com',
        path: endpoint,
        method: 'GET',
        headers: {
            'X-API-Key': API_KEY,
            'Authorization': AUTH_TOKEN
        }
    };
    
    console.log('Making API call with hard-coded credentials...');
    // In real code, this would make the actual HTTP request
    return { success: true, endpoint: endpoint };
}

// Function that encrypts data with hard-coded key
function encryptData(data) {
    const cipher = crypto.createCipher('aes-256-cbc', ENCRYPTION_KEY);
    let encrypted = cipher.update(data, 'utf8', 'hex');
    encrypted += cipher.final('hex');
    return encrypted;
}

// Main execution
console.log('=== CWE-798 Demonstration: Hard-coded Credentials ===\n');

const dbConnection = connectToDatabase();
console.log('Database connection:', dbConnection);

const apiResponse = makeAPICall('/users');
console.log('API response:', apiResponse);

const encrypted = encryptData('sensitive data');
console.log('Encrypted data:', encrypted);

console.log('\n⚠️  WARNING: All credentials are hard-coded in the source code!');
console.log('This is a security vulnerability (CWE-798).');
console.log('Credentials should be stored in environment variables or secure vaults.');
