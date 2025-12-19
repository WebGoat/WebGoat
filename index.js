const marked = require('marked');
const bodyParser = require('body-parser');
const ip = require('ip');
const http = require('http');
const express = require('express');
const path = require('path');
const { exec } = require('child_process');
const pug = require('pug');
let trimNewlines;

// Import the trim-newlines module with proper error handling
import('trim-newlines').then((module) => {
  trimNewlines = module.default;
  console.log("Successfully loaded trim-newlines module");
}).catch(err => {
  console.error("Error loading trim-newlines module:", err);
});

const app = express();
app.use(express.urlencoded({ limit: '10mb', extended: true }));
app.use(express.json({ limit: '10mb' }));
app.use('/static', express.static(path.join(__dirname, 'static')));
app.use(bodyParser.urlencoded({ extended: true }));

// Configure Marked with vulnerable options (for demonstration purposes)
marked.setOptions({
  renderer: new marked.Renderer(),
  sanitize: true, // Note: This is bypassable in version 0.3.5
  mangle: false
});

// Log startup information
console.log('SCAGoat is starting up with the following vulnerabilities:');
console.log('- CVE-2017-16114: Marked v0.3.5 (XSS vulnerability)');
console.log('- CVE-2021-33623: trim-newlines v4.0.0 (ReDoS vulnerability)');
console.log('- Malicious Package: XZ-Java backend demo');
console.log('- CVE-2020-9547: jackson-databind backend demo');
console.log('- CVE-2019-8331: pug v2.0.4 (Denial of Service vulnerability)');
console.log('- CVE-2020-8116: dot-prop v4.2.0 (Prototype pollution vulnerability)');

// Basic routes
app.get('/', function (req, res) {
  res.sendFile(__dirname + '/templates/index.html');
});

app.get('/markdown', function (req, res) {
  res.sendFile(__dirname + '/templates/markdown.html');
});

app.get('/chat-ui', function (req, res) {
  res.sendFile(__dirname + '/templates/chat-ui.html');
});

app.get('/lodash', function (req, res) {
  res.sendFile(__dirname + '/templates/lodash.html');
});

app.get('/trimnewlines', function (req, res) {
  res.send(`
   <!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Trim-Newlines Demo - SCAGoat</title>
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.16/dist/tailwind.min.css" rel="stylesheet">
  <link href="../static/css/style.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <style>
    textarea {
      overflow: hidden;
    }
    .vuln-notice {
      background-color: rgba(220, 38, 38, 0.8);
      color: white;
      border-radius: 8px;
      padding: 16px;
      margin-bottom: 20px;
    }
  </style>
</head>
<body>
  <!-- Particle.js Background -->
  <div id="particles-js"></div>

  <nav class="navbar">
      <a href="/"><i class="fas fa-home"></i> Home</a>
      <a href="/markdown"><i class="fas fa-file-alt"></i> Markdown</a>
      <a href="/checkip"><i class="fas fa-search"></i> Checkip</a>
      <a href="/trimnewlines" class="active"><i class="fas fa-cut"></i> Trim-Newlines</a>
      <a href="/chat-ui"><i class="fas fa-comment"></i> Chat</a>
      <a href="http://localhost:8080" target="_blank"><i class="fas fa-server"></i> Backend</a>
  </nav>

  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-white text-center mb-8">Trim-Newlines Vulnerability Demo</h1>
    
    <div class="max-w-3xl mx-auto bg-gray-800 bg-opacity-80 p-6 rounded-lg shadow-md">
      <div class="vuln-notice mb-4">
        <h2 class="text-xl font-bold mb-2"><i class="fas fa-exclamation-triangle mr-2"></i>CVE-2021-33623</h2>
        <p class="mb-2">This demonstrates a Regular Expression Denial of Service (ReDoS) vulnerability in the trim-newlines npm package version 4.0.0.</p>
        <p>Try entering multiple newline characters followed by a single character to trigger the vulnerability.</p>
      </div>
      
      <form action="/trimnewlines" method="post" class="space-y-4">
        <div>
          <label for="payload" class="block text-white text-sm font-bold mb-2">Enter Payload:</label>
          <div class="relative">
            <textarea id="payload" name="payload" rows="6" class="w-full px-3 py-2 rounded-md border border-gray-600 bg-gray-700 text-white focus:outline-none focus:border-blue-500" placeholder="Enter payload with newlines (e.g. '\\n\\n\\n\\na')"></textarea>
            <button type="button" id="insertPayload" class="absolute top-2 right-2 bg-yellow-500 text-white text-xs px-2 py-1 rounded hover:bg-yellow-600">Insert Vulnerable Payload</button>
          </div>
        </div>
        <div class="text-center">
          <button type="submit" class="bg-red-600 text-white px-6 py-3 rounded-md hover:bg-red-700 focus:outline-none transform transition hover:scale-105">
            <i class="fas fa-bug mr-2"></i>Test Vulnerability
          </button>
        </div>
      </form>
    </div>
  </div>

  <!-- Particle.js Library -->
  <script src="https://cdn.jsdelivr.net/particles.js/2.0.0/particles.min.js"></script>
  <script>
    // Particle.js configuration - same as index page
    particlesJS('particles-js', {
      "particles": {
        "number": {"value": 80, "density": {"enable": true, "value_area": 800}},
        "color": {"value": "#ffffff"},
        "shape": {
          "type": "circle",
          "stroke": {"width": 0, "color": "#000000"},
          "polygon": {"nb_sides": 5}
        },
        "opacity": {
          "value": 0.5, "random": false,
          "anim": {"enable": false, "speed": 1, "opacity_min": 0.1, "sync": false}
        },
        "size": {
          "value": 3, "random": true,
          "anim": {"enable": false, "speed": 40, "size_min": 0.1, "sync": false}
        },
        "line_linked": {
          "enable": true, "distance": 150, "color": "#ffffff", "opacity": 0.4, "width": 1
        },
        "move": {
          "enable": true, "speed": 6, "direction": "none", "random": false, "straight": false,
          "out_mode": "out", "bounce": false,
          "attract": {"enable": false, "rotateX": 600, "rotateY": 1200}
        }
      },
      "interactivity": {
        "detect_on": "canvas",
        "events": {
          "onhover": {"enable": true, "mode": "grab"},
          "onclick": {"enable": true, "mode": "push"},
          "resize": true
        },
        "modes": {
          "grab": {"distance": 140, "line_linked": {"opacity": 1}},
          "bubble": {"distance": 400, "size": 40, "duration": 2, "opacity": 8, "speed": 3},
          "repulse": {"distance": 200, "duration": 0.4},
          "push": {"particles_nb": 4},
          "remove": {"particles_nb": 2}
        }
      },
      "retina_detect": true
    });

    // Insert payload functionality
    document.addEventListener('DOMContentLoaded', function() {
      document.getElementById('insertPayload').addEventListener('click', function() {
        // Creates a ReDoS vulnerable payload for trim-newlines
        let payload = '\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\n\\na';
        document.getElementById('payload').value = payload;
      });
    });
  </script>
</body>
</html>
  `);
});

app.post('/trimnewlines', function (req, res) {
    const payload = req.body.payload;
    const maxLength = 1e7;
    const string = String(Array.from({length: Math.min(maxLength, 150)}).fill(payload).join('')) + 'a';
    
    console.log(`Processing trim-newlines request with payload length: ${payload.length}`);
    
    const start = Date.now();
    let trimmedString = "";
    
    try {
      trimmedString = trimNewlines.end(string);
      console.log(`Trimmed string length: ${trimmedString.length}`);
    } catch (error) {
      console.error("Error in trim-newlines processing:", error);
      trimmedString = "Error processing the input: " + error.message;
    }
    
    const difference = Date.now() - start;
    console.log(`Execution time difference: ${difference}ms`);
    
    let responseMessage;
    let vulnerabilityStatus = "normal";
    
    if (difference > 100) {
        console.log("\nExponential execution time -> VULNERABLE\n");
        responseMessage = `\nExponential execution time -> VULNERABLE. Execution time: ${difference}ms\n`;
        vulnerabilityStatus = "vulnerable";
    } else {
        responseMessage = `\nExecution time: ${difference}ms\n`;
    }
    
    res.send(`
    <html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trim-Newlines Result - SCAGoat</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
        <link href="../static/css/style.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <style>
            #trimmedString {
                display: none;
            }
            #readMore:target #trimmedString {
                display: block;
            }
            #readMore:target #readMoreLink {
                display: none;
            }
        </style>
    </head>
    <body>
        <!-- Particle.js Background -->
        <div id="particles-js"></div>
        
        <nav class="navbar">
            <a href="/"><i class="fas fa-home"></i> Home</a>
            <a href="/markdown"><i class="fas fa-file-alt"></i> Markdown</a>
            <a href="/checkip"><i class="fas fa-search"></i> Checkip</a>
            <a href="/trimnewlines" class="active"><i class="fas fa-cut"></i> Trim-Newlines</a>
            <a href="/chat-ui"><i class="fas fa-comment"></i> Chat</a>
            <a href="http://localhost:8080" target="_blank"><i class="fas fa-server"></i> Backend</a>
        </nav>
        
        <div class="flex items-center justify-center min-h-screen">
            <div class="text-center p-8 bg-gray-800 bg-opacity-80 rounded-lg shadow-md max-w-lg w-full">
                <h1 class="text-2xl font-bold mb-4 text-white">Trim-Newlines Result</h1>
                
                ${vulnerabilityStatus === "vulnerable" ? 
                  `<div class="bg-red-600 bg-opacity-80 text-white p-4 rounded-md mb-4">
                    <i class="fas fa-exclamation-triangle text-2xl mb-2"></i>
                    <h2 class="text-xl font-bold">Vulnerability Detected!</h2>
                    <p>The trim-newlines package is vulnerable to ReDoS attacks.</p>
                  </div>` : 
                  ''}
                
                <div class="bg-gray-700 p-4 rounded-md text-white mb-4">
                    ${responseMessage}
                </div>
                
                <div id="readMore" class="mb-4">
                    <a id="readMoreLink" href="#readMore" class="inline-block bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md transition">
                        <i class="fas fa-eye mr-2"></i>Show Trimmed String
                    </a>
                    <div id="trimmedString" class="mt-4 bg-gray-900 p-4 rounded-md text-gray-300 text-left overflow-x-auto">
                        <pre class="whitespace-pre-wrap">${trimmedString}</pre>
                        <a href="#" class="inline-block bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md mt-4 transition">
                            <i class="fas fa-eye-slash mr-2"></i>Hide Trimmed String
                        </a>
                    </div>
                </div>
                
                <button onclick="location.href='/trimnewlines'" class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-md transition transform hover:scale-105">
                    <i class="fas fa-redo mr-2"></i>Try Another Payload
                </button>
            </div>
        </div>
        
        <!-- Particle.js Library -->
        <script src="https://cdn.jsdelivr.net/particles.js/2.0.0/particles.min.js"></script>
        <script>
            // Particle.js configuration - same as before
            particlesJS('particles-js', {
                "particles": {
                    "number": {"value": 80, "density": {"enable": true, "value_area": 800}},
                    "color": {"value": "#ffffff"},
                    "shape": {
                        "type": "circle",
                        "stroke": {"width": 0, "color": "#000000"},
                        "polygon": {"nb_sides": 5}
                    },
                    "opacity": {
                        "value": 0.5, "random": false,
                        "anim": {"enable": false, "speed": 1, "opacity_min": 0.1, "sync": false}
                    },
                    "size": {
                        "value": 3, "random": true,
                        "anim": {"enable": false, "speed": 40, "size_min": 0.1, "sync": false}
                    },
                    "line_linked": {
                        "enable": true, "distance": 150, "color": "#ffffff", "opacity": 0.4, "width": 1
                    },
                    "move": {
                        "enable": true, "speed": 6, "direction": "none", "random": false, "straight": false,
                        "out_mode": "out", "bounce": false,
                        "attract": {"enable": false, "rotateX": 600, "rotateY": 1200}
                    }
                },
                "interactivity": {
                    "detect_on": "canvas",
                    "events": {
                        "onhover": {"enable": true, "mode": "grab"},
                        "onclick": {"enable": true, "mode": "push"},
                        "resize": true
                    },
                    "modes": {
                        "grab": {"distance": 140, "line_linked": {"opacity": 1}},
                        "bubble": {"distance": 400, "size": 40, "duration": 2, "opacity": 8, "speed": 3},
                        "repulse": {"distance": 200, "duration": 0.4},
                        "push": {"particles_nb": 4},
                        "remove": {"particles_nb": 2}
                    }
                },
                "retina_detect": true
            });
        </script>
    </body>
</html>
    `);
});

app.post('/markdown', function (req, res) {
    var fullName = req.body.fullName;
    var email = req.body.email;
    var markdownResume = req.body.markdownResume;
    
    console.log(`Processing markdown request for ${fullName}`);
    
    // Process the markdown resume here
    var htmlResume = '';
    try {
      htmlResume = marked(markdownResume); // Convert Markdown to HTML
      console.log("Markdown successfully converted to HTML");
    } catch (error) {
      console.error("Error converting markdown:", error);
      htmlResume = `<p class="text-red-500">Error rendering markdown: ${error.message}</p>`;
    }
    
    var htmlResponse = `
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${fullName} - Resume</title>
            <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
            <link href="../static/css/style.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        </head>
        <body>
            <!-- Particle.js Background -->
            <div id="particles-js"></div>
            
            <nav class="navbar">
                <a href="/"><i class="fas fa-home"></i> Home</a>
                <a href="/markdown" class="active"><i class="fas fa-file-alt"></i> Markdown</a>
                <a href="/checkip"><i class="fas fa-search"></i> Checkip</a>
                <a href="/trimnewlines"><i class="fas fa-cut"></i> Trim-Newlines</a>
                <a href="/chat-ui"><i class="fas fa-comment"></i> Chat</a>
                <a href="http://localhost:8080" target="_blank"><i class="fas fa-server"></i> Backend</a>
            </nav>
            
            <div class="container mx-auto px-4 py-8">
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-3xl font-bold text-white">${fullName}'s Resume</h1>
                    <div class="text-white text-lg">${email}</div>
                </div>
                
                <div class="bg-white bg-opacity-95 p-8 rounded-lg shadow-lg">
                    <div class="prose max-w-none">${htmlResume}</div>
                </div>
                
                <div class="mt-8 bg-red-900 bg-opacity-70 p-4 rounded-lg text-white">
                    <h2 class="text-xl font-bold mb-2"><i class="fas fa-exclamation-triangle mr-2"></i>Vulnerability Information</h2>
                    <p>This page demonstrates CVE-2017-16114 - a Cross-Site Scripting (XSS) vulnerability in the Marked library v0.3.5.</p>
                    <p>The markdown has been processed with <code>sanitize: true</code>, but this sanitization can be bypassed with certain payloads.</p>
                </div>
                
                <div class="text-center mt-6">
                    <a href="/markdown" class="inline-block bg-blue-600 text-white px-6 py-3 rounded-md hover:bg-blue-700">
                        <i class="fas fa-arrow-left mr-2"></i>Back to Form
                    </a>
                </div>
            </div>
            
            <!-- Particle.js Library -->
            <script src="https://cdn.jsdelivr.net/particles.js/2.0.0/particles.min.js"></script>
            <script>
                // Particle.js configuration - same as before
                particlesJS('particles-js', {
                    "particles": {
                        "number": {"value": 80, "density": {"enable": true, "value_area": 800}},
                        "color": {"value": "#ffffff"},
                        "shape": {
                            "type": "circle",
                            "stroke": {"width": 0, "color": "#000000"},
                            "polygon": {"nb_sides": 5}
                        },
                        "opacity": {
                            "value": 0.5, "random": false,
                            "anim": {"enable": false, "speed": 1, "opacity_min": 0.1, "sync": false}
                        },
                        "size": {
                            "value": 3, "random": true,
                            "anim": {"enable": false, "speed": 40, "size_min": 0.1, "sync": false}
                        },
                        "line_linked": {
                            "enable": true, "distance": 150, "color": "#ffffff", "opacity": 0.4, "width": 1
                        },
                        "move": {
                            "enable": true, "speed": 6, "direction": "none", "random": false, "straight": false,
                            "out_mode": "out", "bounce": false,
                            "attract": {"enable": false, "rotateX": 600, "rotateY": 1200}
                        }
                    },
                    "interactivity": {
                        "detect_on": "canvas",
                        "events": {
                            "onhover": {"enable": true, "mode": "grab"},
                            "onclick": {"enable": true, "mode": "push"},
                            "resize": true
                        },
                        "modes": {
                            "grab": {"distance": 140, "line_linked": {"opacity": 1}},
                            "bubble": {"distance": 400, "size": 40, "duration": 2, "opacity": 8, "speed": 3},
                            "repulse": {"distance": 200, "duration": 0.4},
                            "push": {"particles_nb": 4},
                            "remove": {"particles_nb": 2}
                        }
                    },
                    "retina_detect": true
                });
            </script>
        </body>
        </html>
    `;
    
    res.send(htmlResponse);
});

app.get('/checkip', function(req, res) {
    res.send(`
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Check IP - SCAGoat</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
        <link href="../static/css/style.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <style>
            /* Loading indicator styles */
            .loading-container {
                width: 100px;
                height: 100px;
                position: relative;
                margin: 0 auto;
            }
    
            .sand-timer-icon {
                font-size: 60px;
                color: #f3f4f6;
                position: absolute;
                top: 20px;
                left: 20px;
                animation: sand-timer-rotation 2s linear infinite;
            }
    
            @keyframes sand-timer-rotation {
                0% {
                    transform: rotate(0deg);
                }
                100% {
                    transform: rotate(360deg);
                }
            }
    
            .error-message {
                color: #ef4444;
                font-size: 0.875rem;
                margin-top: 0.25rem;
            }
        </style>
    </head>
    <body>
        <!-- Particle.js Background -->
        <div id="particles-js"></div>
        
        <nav class="navbar">
            <a href="/"><i class="fas fa-home"></i> Home</a>
            <a href="/markdown"><i class="fas fa-file-alt"></i> Markdown</a>
            <a href="/checkip" class="active"><i class="fas fa-search"></i> Checkip</a>
            <a href="/trimnewlines"><i class="fas fa-cut"></i> Trim-Newlines</a>
            <a href="/chat-ui"><i class="fas fa-comment"></i> Chat</a>
            <a href="http://localhost:8080" target="_blank"><i class="fas fa-server"></i> Backend</a>
        </nav>
    
        <div class="container mx-auto px-4 py-8">
            <h1 class="text-3xl font-bold text-white text-center mb-8">Check IP Address</h1>
            
            <div class="max-w-lg mx-auto bg-gray-800 bg-opacity-80 p-6 rounded-lg shadow-md">
                <div class="mb-4 text-white">
                    <h2 class="text-xl font-bold mb-2"><i class="fas fa-exclamation-triangle text-yellow-500 mr-2"></i>SSRF Vulnerability Demo</h2>
                    <p class="mb-2">This page demonstrates a Server-Side Request Forgery (SSRF) vulnerability through the IP validation mechanism.</p>
                    <p>Try entering a local IP address or URL to access internal resources.</p>
                </div>
            
                <form id="checkIpForm" class="text-gray-800">
                    <div class="mb-4">
                        <label for="ip" class="block text-white text-sm font-bold mb-2">Enter IP Address or Hostname</label>
                        <input type="text" id="ip" name="ip" class="w-full px-3 py-2 rounded-md border border-gray-600 bg-gray-700 text-white focus:outline-none focus:border-blue-500" placeholder="Enter IP address or hostname" required>
                        <div id="errorMessage" class="error-message"></div>
                    </div>
                    <div id="loadingContainer" class="loading-container hidden">
                        <i class="fas fa-spinner fa-spin sand-timer-icon"></i>
                    </div>
                    <div id="responseContainer" class="mb-4 bg-gray-900 p-4 rounded-md text-white hidden overflow-auto max-h-80"></div>
                    <div class="text-center">
                        <button type="button" onclick="checkIp()" class="bg-blue-600 text-white px-6 py-3 rounded-md hover:bg-blue-700 focus:outline-none focus:bg-blue-700 transform transition hover:scale-105">
                            <i class="fas fa-search mr-2"></i>Check IP
                        </button>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- Particle.js Library -->
        <script src="https://cdn.jsdelivr.net/particles.js/2.0.0/particles.min.js"></script>
        <script>
            // Particle.js configuration - same as before
            particlesJS('particles-js', {
                "particles": {
                    "number": {"value": 80, "density": {"enable": true, "value_area": 800}},
                "color": {"value": "#ffffff"},
                "shape": {
                    "type": "circle",
                    "stroke": {"width": 0, "color": "#000000"},
                    "polygon": {"nb_sides": 5}
                },
                "opacity": {
                    "value": 0.5, "random": false,
                    "anim": {"enable": false, "speed": 1, "opacity_min": 0.1, "sync": false}
                },
                "size": {
                    "value": 3, "random": true,
                    "anim": {"enable": false, "speed": 40, "size_min": 0.1, "sync": false}
                },
                "line_linked": {
                    "enable": true, "distance": 150, "color": "#ffffff", "opacity": 0.4, "width": 1
                },
                "move": {
                    "enable": true, "speed": 6, "direction": "none", "random": false, "straight": false,
                    "out_mode": "out", "bounce": false,
                    "attract": {"enable": false, "rotateX": 600, "rotateY": 1200}
                }
                },
                "interactivity": {
                    "detect_on": "canvas",
                    "events": {
                        "onhover": {"enable": true, "mode": "grab"},
                        "onclick": {"enable": true, "mode": "push"},
                        "resize": true
                    },
                    "modes": {
                        "grab": {"distance": 140, "line_linked": {"opacity": 1}},
                        "bubble": {"distance": 400, "size": 40, "duration": 2, "opacity": 8, "speed": 3},
                        "repulse": {"distance": 200, "duration": 0.4},
                        "push": {"particles_nb": 4},
                        "remove": {"particles_nb": 2}
                    }
                },
                "retina_detect": true
            });
            
            function checkIp() {
                var ip = document.getElementById('ip').value;
                var errorMessage = document.getElementById('errorMessage');
                var responseContainer = document.getElementById('responseContainer');
                
                if (ip.trim() === '') {
                    errorMessage.textContent = 'Please enter an IP address or hostname.';
                    responseContainer.innerHTML = '';
                    responseContainer.classList.add('hidden');
                    return;
                }
                
                errorMessage.textContent = '';
                document.getElementById('loadingContainer').classList.remove('hidden');
                responseContainer.innerHTML = '';
                responseContainer.classList.add('hidden');
                
                setTimeout(function() {
                    var xhr = new XMLHttpRequest();
                    xhr.open('POST', '/checkip');
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.onload = function() {
                        document.getElementById('loadingContainer').classList.add('hidden');
                        responseContainer.classList.remove('hidden');
                        
                        if (xhr.status === 200) {
                            responseContainer.innerHTML = xhr.responseText;
                            
                            // Syntax highlighting for HTML content
                            if (xhr.responseText.trim().startsWith('<!DOCTYPE') || 
                                xhr.responseText.trim().startsWith('<html')) {
                                responseContainer.innerHTML = '<div class="text-green-400 font-bold mb-2">HTML content fetched successfully:</div>' +
                                    '<pre class="text-xs">' + escapeHtml(xhr.responseText) + '</pre>';
                            }
                        } else {
                            responseContainer.innerHTML = '<div class="text-red-500">Error: ' + xhr.status + '</div>';
                        }
                    };
                    xhr.onerror = function() {
                        document.getElementById('loadingContainer').classList.add('hidden');
                        responseContainer.classList.remove('hidden');
                        responseContainer.innerHTML = '<div class="text-red-500">Network error occurred</div>';
                    };
                    xhr.send('ip=' + encodeURIComponent(ip));
                }, 1000); // Delay for 1 second
            }
            
            // Escape HTML for safe display
            function escapeHtml(unsafe) {
                return unsafe
                    .replace(/&/g, "&amp;")
                    .replace(/</g, "&lt;")
                    .replace(/>/g, "&gt;")
                    .replace(/"/g, "&quot;")
                    .replace(/'/g, "&#039;");
            }
            
            // Prevent form submission on Enter key press
            document.getElementById('checkIpForm').addEventListener('submit', function(event) {
                event.preventDefault();
                checkIp();
            });
            
            // Also handle enter key on the input
            document.getElementById('ip').addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    checkIp();
                }
            });
        </script>
    </body>
    </html>
    `);
});

// Handling POST request to process the form submission
app.post('/checkip', function (req, res) {
    var my_ip = req.body.ip;
    var responseMessage = '';

    console.log(`Processing IP check request for: ${my_ip}`);

    // Check if the IP is public
    try {
        if (ip.isPublic(my_ip)) {
            responseMessage = `${my_ip} is a public IP address.`;
            console.log(`${my_ip} identified as public, attempting to fetch content`);
            
            // Try to fetch content from the IP
            import('node-fetch').then(({ default: fetch }) => {
                // Add http:// prefix if not present
                let url = my_ip;
                if (!url.startsWith('http://') && !url.startsWith('https://')) {
                    url = 'http://' + url;
                }
                
                console.log(`Fetching from URL: ${url}`);
                
                fetch(url + "/index.html")
                    .then(response => {
                        console.log(`Response status: ${response.status}`);
                        return response.text();
                    })
                    .then(html => {
                        console.log(`Received ${html.length} bytes of content`);
                        responseMessage = html;
                        res.send(responseMessage);
                    })
                    .catch(error => {
                        console.error(`Error fetching content: ${error.message}`);
                        responseMessage = `Error fetching from ${url}: ${error.message}`;
                        res.send(responseMessage);
                    });
            }).catch(err => {
                console.error(`Error importing node-fetch: ${err.message}`);
                responseMessage = `Error: Could not import fetch library. ${err.message}`;
                res.send(responseMessage);
            });
        } else if (ip.isPrivate(my_ip)) {
            // If private, display a message indicating it's a private IP
            console.log(`${my_ip} identified as private`);
            responseMessage = `${my_ip} is a private IP address.`;
            res.send(responseMessage);
        } else {
            // If the IP is neither public nor private, attempt to treat as hostname
            console.log(`${my_ip} is not a valid IP, treating as hostname`);
            responseMessage = `${my_ip} is not recognized as an IP address. Treating as hostname.`;
            
            // Try to fetch content from the hostname
            import('node-fetch').then(({ default: fetch }) => {
                // Add http:// prefix if not present
                let url = my_ip;
                if (!url.startsWith('http://') && !url.startsWith('https://')) {
                    url = 'http://' + url;
                }
                
                console.log(`Fetching from URL: ${url}`);
                
                fetch(url)
                    .then(response => {
                        console.log(`Response status: ${response.status}`);
                        return response.text();
                    })
                    .then(html => {
                        console.log(`Received ${html.length} bytes of content`);
                        responseMessage = html;
                        res.send(responseMessage);
                    })
                    .catch(error => {
                        console.error(`Error fetching content: ${error.message}`);
                        responseMessage = `Error fetching from ${url}: ${error.message}`;
                        res.send(responseMessage);
                    });
            }).catch(err => {
                console.error(`Error importing node-fetch: ${err.message}`);
                responseMessage = `Error: Could not import fetch library. ${err.message}`;
                res.send(responseMessage);
            });
        }
    } catch (error) {
        console.error(`Error processing IP check: ${error.message}`);
        responseMessage = `Error: ${error.message}`;
        res.send(responseMessage);
    }
});

app.get('/pug', function (req, res) {
  res.sendFile(__dirname + '/templates/pug.html');
});

app.get('/dotprop', function (req, res) {
  res.sendFile(__dirname + '/templates/dotprop.html');
});

// Add the API endpoint for rendering Pug templates
app.post('/api/render-pug', function (req, res) {
  const template = req.body.template;
  const data = req.body.data || {};
  
  console.log('Rendering Pug template with data:', data);
  
  try {
    // Set a timeout to prevent DoS attacks (demonstrating the vulnerability)
    const timeoutMs = 5000; // 5 seconds timeout
    let timeoutId;
    
    const timeoutPromise = new Promise((_, reject) => {
      timeoutId = setTimeout(() => {
        reject(new Error('Rendering timed out - potential DoS detected'));
      }, timeoutMs);
    });
    
    // Rendering promise
    const renderPromise = new Promise((resolve) => {
      const startTime = Date.now();
      
      try {
        // Compile and render the template
        const compiledFunction = pug.compile(template);
        const html = compiledFunction(data);
        
        const endTime = Date.now();
        const renderTime = endTime - startTime;
        
        console.log(`Pug template rendered in ${renderTime}ms`);
        
        resolve({
          html,
          renderTime
        });
      } catch (err) {
        resolve({
          error: err.message
        });
      }
    });
    
    // Race between rendering and timeout
    Promise.race([renderPromise, timeoutPromise])
      .then((result) => {
        clearTimeout(timeoutId);
        
        if (result.error) {
          res.json({
            error: result.error
          });
        } else {
          res.json({
            html: result.html,
            renderTime: result.renderTime
          });
        }
      })
      .catch((err) => {
        console.error('Error rendering Pug template:', err);
        res.json({
          error: err.message
        });
      });
  } catch (err) {
    console.error('Error in Pug rendering endpoint:', err);
    res.json({
      error: 'Server error: ' + err.message
    });
  }
});

// Add API endpoint for dotprop
app.post('/api/dotprop', function (req, res) {
  const path = req.body.path;
  const value = req.body.value;
  
  console.log(`Demonstration of dot-prop with path: ${path} and value: ${value}`);
  
  try {
    // Import the vulnerable dot-prop version
    const dotProp = require('dot-prop');
    
    // Create a test object
    const originalObject = {
      user: {
        name: 'Default User',
        id: 1,
        permissions: {
          read: true,
          write: false
        }
      },
      app: {
        version: '1.0.0',
        settings: {
          theme: 'dark'
        }
      }
    };
    
    // Clone the object to avoid modifying the original during demonstrations
    const modifiedObject = JSON.parse(JSON.stringify(originalObject));
    
    // Use the vulnerable set function
    dotProp.set(modifiedObject, path, value);
    
    // Check if we've polluted the prototype
    const newEmptyObject = {};
    
    // Return the results
    res.json({
      originalObject,
      modifiedObject,
      newObject: newEmptyObject
    });
    
  } catch (err) {
    console.error('Error in dot-prop demonstration:', err);
    res.json({
      error: 'Error: ' + err.message
    });
  }
});

app.listen(3000, function () {
    const asciiArt = `
................................................,,::;;;++*******+++;;::,,...............................................
...............................,+??+,..,,:;+?%SS##@@#####SSSSSSS#####@@@##S%?+;:,,..,+??;...............................
...............................;@@@@S*;*#@@##S%?***++++++++++++++++++++**?%S##@@S*;?S@@@@;..............................
...............................+#%S###%??;;++++;;;;+*?+,,;?*,,;+,,+;:+*;;*++++;+??S###S%@;..............................
...........................:*%+?S+S#%*?%%?+;*??:.;;.;*,,:.+*.,,.,.+,,,;.:???*;+?%%?*S#%*#**%+:..........................
.......................,;?S@@S;S%+%#S:.:?%%*:;*+.;;,;;,;+;:+:++*+:+,*:..+?+::?%%?:.;SS?+S%+S@@S*;,......................
....................,;%#@#%*;;:#?;?SS+..,?%??;:;;+++*++***+++++*++****++*:,;?*S?,.,+#S?;%S:;;*%#@#?;,...................
...................:#@#?+;;+**:S%:?S#+...:#***+::;??%%SSS#######SSS%%??;:;+?*?S:..,*#S?:S%:**+;;+%@@S,..................
...................%@#;++*****:?S,*%#%:..,%#;?+*+;+S@@@@@@@@@@@@@@@@@S+;+*+?;#?,..;S#%+:#*;*+***+;;@@*..................
..................:@@*;*******++#:;%#%;,.:%#:+*???*;%@####SSSSS###@@?;*??**+:#%:.,;S#?:%%:+:;;;****;@@#,.................
..................?@#:****:+*?*:#?:%#%:,,+#%:*?%S#@S??*+;;;;;;;;;+**?#@#S%?*:S#+,,:%#?:%%:+:;;;****;@@*.................
.................:#@?;**+;+++*;??#++#S;,:*@*;%#@@#%+:,,,,,,,,,,,,,,,;+%@@@S%;?@*:,;SS;*S*+++::;;**?:%@S,................
.................*@@;*?*:+*??;?@S*S+*#S+;+##%#@@?;,,:::;;;;;;;;;;;;::,,;?@@#%#S+;+SS+*S*#@*+*;,:+*?++@@;................
.................S@%:?*;:++*;?@@@#*%?*S##S#@@@S+:::;;;;;;;;;;;;;;;;;;;:::*S@@@#S##%*%%?#@@@*+*:::;??:#@%................
................:@@*+?+:::*++@@@@@@%%%%%S#@@@S+::;;+++++++++++++++++++;;::+S@@@#S%%%%%@@@@@@+*;;;++?;?@#,...............
................*@@;**;;:+*;#@@@@@@@@S%SSS#@S+::;;++******?????*****++;;;::+S@#S%S%S@@@@@@@@S:?+,:+?*;@@+...............
................S@S:?+++;*;?@#@@#@@#@@@#S%@#*:,,,:;+*???%%%%%%%%%??**+;:,,,:*#@%S#@@@@@@#@##@*+*::;+?:S@%...............
...............:@@?;?:+;;*;@@###@#@#@@@@@?#*:,:;;+***?%SSSSSSSSSS%%?*+++;::,:*#?@@@@#@@#@###@#;?;:+*?;?@#,..............
...............+@@+**;;;*;?@@@S?@S%@##@@S%S;+%SS###SS%SSSSSSSSSSSSS%SSS##SS?+;S%#@@##@%S@?S@@@*+*;::***@@;..............
...............?@#;*+;;;?;#@@@S*%S*%@##@%S?+S@@@S;*@#SSSSS%%%%%%SSSS#@*:%@@@S+%SS@##@?*S%?S@@@S;?;++**+@@*..............
...............%@#:*;;:**;##SS#SS@@S#@@@SS%+S####SS@@SS%%?*****??%SS@@SS####S+%SS@@@#S@#SS#SS##;?;;++?;@@%..............
...............S@S;?+*+?++S?***%?#@@##@@#%S+*S%*%##@@S%**+++;++++*%S@@@#%*%S++S%@@@##@@S?**+*%S;*+;++?;#@S..............
..............,#@%;????SSS########@@@#S@@%S*;?S%%%%SS%*+*;;:::;+++*%SS%%%%S*:*SS@@S#@@@########SS%????;#@S..............
..............,#@%;???S@S*?*?*?*+S@@@@#@@@%S+:;+*??%S*:?S*+;;;+?S*:*S%?**+;:+S%@@##@@@@%+??*?*?*S@%???;S@S,...,.........
..............,#@S;??S@?;;?*?+?*:%@S##%#@@#%S*;+**%#*,:%@#S???S#@?:,*#%**+;?#S@@@S%##S@?:*****%;;?@S??;#@S..............
...............S@S:????%?????????S@SSS%%S@@#SSS%%%#@+.,:+S@@@@@%+:,.+@#%%%SS%#@@S%%SSS@S?????????%%%??;#@S..............
...............%@#;??%S@@########@@@@@%S@@@#?#@@###@S;,...;S@S;...,;S@##@@@#*#@@@SS@@@@@########@@S%??;@@%..............
...............?@@+*%S@@#%*****?%#@@@@@@@@S+##%S#@@@@S*;;+?SSS?+;;*S@@@@#S?#S*@@@@@@@@@@S%**+??%#@@S%*+@@*..............
...............;@@?;%?%#@########@@@@@@S%**S#%*?%S#@@@@@@#?+;+?#@@@@@@#S%?*?@??S?++;+*S@########@#%?%+?@@;..............
...............,#@S:%*;?S+#@@@@@SS####%%%?#@S%%%%S##%?%%#@@@#@@@@@@@##S%%%%S#@?SS*:::,,*S#@@@@#+S?;+%:S@#,..............
................?@@+*?:+%+*@@@@%SS%%%%%SSSSSSSS##@#?;:,,:+#@@@@@@###SSS##SSSSSS#@#?+++++%*#@@@*+%;;**;@@?...............
................:@@%;%+:??;#@@@%#?..,,,,,::;;++*?S@@?;;;::+#SSSSSS%%%%??**+;;;:*#@#??%?%#%%@@#;?*;;?;%@@;...............
.................%@@+**;+?+?@@@SSS..%#%%%??*++;::+#@?**++++S@*;;::,,,,:::;;++*?%#@@#S###@%S@@?+%+;**+@@%................
.................;@@#;?++??;#@@#%#,.%#+;;;+++*%@SS#@SS%??%%#@**?%%%%%%%%##SSS#S*?%S@@@@#S?@@#;%?;+?;#@@:................
..................?@@%+%**%+*@@@%@+,*@+,,,,,:*%+::+#@##SS##@S??*?%*+++++%#???*+***%@#SS@?#@@**?+*%;%@@?.................
..................,S@@S%%SS%;%@@%@%,:#%;;;:*??*;,,,*#@@##@@#%*++++??*+*+%S;*****??%@+:?@?@@%+%S%%SS@@#,.................
...................:#@#%?+?#%*@@S%@;:?@*:::;:,,++,,,;?%##?+?@%+++++*??*?S?*****???#S:;#%S@@*%#%*?%#@#:..................
....................;@S;;;;%#S#@@S@S;;#S;::::,;::;;:::,:*;:?%%%?*****?#?+****????%@+:?@S@@#S#%;;;;S@;...................
.....................;#S++;;%#S%?*%@%;+#S;::;?:,:;:,;+;+:::+*?S%??%?**#%*???????%@?:*@S*?%S#%;;;+S#;....................
......................;##*++++++++?S@%;+#S?++*??;::;:,,:,,+%##%?***?%S%?%%%S%%%S@?;*@#?++++++++*##:.....................
.......................:S@?+++++++?%S@%++S#*:,:**+*:,,,,::,,:*S@#S?%@?****??%S##*;*@#%?+++++++?@S:......................
.........................*@S*+**+**??S@#*+?#%+::;;*+;;;;+?%%???****#S*??????%#%++%@#%?*++**+*S@*,.......................
..........................:S@%%%?***??%#@%**S#%+::::::+:;+?%%%%??*%@?%%%%%%#S*+?#@S??****%%%@S:.........................
...........................,*#@@#%?***?%S##%**%S%*;::*:::,,,:+S#S%S#???%S#S?*?S@#%?***?%#@@#+,..........................
.............................,*#@@#S%**??%S@#%**%SS?*+::::;?SS%??%SS#SSS%**?S@#%??**?S#@@#*,............................
...............................,*#@@@#%?????S##S???%SS?*;:::;SS##S%S#S?*?%##S%????%#@@@S*,..............................
.................................,;%@@@@#S%???%S##S%???%S%S%*+S##SS%???%###%???%%#@@@@%;,................................
....................................:*S@@@@#S%%??%S##S%???%S%S%???%S###S%??%S#@@@@S*:...................................
.......................................:+%#@@@@#S%%?%SS###S%%%SS###S%%%%S##@@@@S*:......................................
..........................................,;?S#@@@@#S%%%%%S###SS%%%%S#@@@@@#?+,.........................................
..............................................,;*%S@@@@@#SS%%%SS##@@@@#S?+:,............................................
..................................................,,;*?S#@@@@@@@#S%*+:,.................................................
........................................................,:;+**;:,.......................................................
    `;
    
    console.log(asciiArt);
    console.log("SCAGoat is running at http://localhost:3000");
    console.log("Backend API is running at http://localhost:8080");
});
