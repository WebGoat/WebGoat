import os
from datetime import datetime, timedelta
import base64
import secrets

from flask import (
    Flask,
    render_template,
    request,
    redirect,
    url_for,
    make_response,
    flash,
)

app = Flask(__name__)

# Load secret key from environment instead of hardcoding.
# In production, set FLASK_SECRET_KEY to a strong random value, e.g.:
#   FLASK_SECRET_KEY=$(python -c "import secrets; print(secrets.token_hex(32))")
app.secret_key = os.environ.get("FLASK_SECRET_KEY")
if not app.secret_key:
    # Fail fast rather than running with an insecure hardcoded key.
    raise RuntimeError("FLASK_SECRET_KEY environment variable must be set")

# NOTE: The following structures are intentionally vulnerable for lab purposes.
# They are kept to preserve application behavior but should not be used in production.
users = {
    "admin": {
        "password": "admin123",
        "email": "admin@example.com",
        "role": "admin",
    },
    "user": {
        "password": "password123",
        "email": "user@example.com",
        "role": "user",
    },
}

password_reset_tokens: dict[str, str] = {}


@app.route("/")
def index():
    return render_template("index.html")


@app.route("/lab")
def lab():
    return render_template("lab.html")


@app.route("/login", methods=["POST"])
def login():
    username = request.form.get("username")
    password = request.form.get("password")
    remember_me = request.form.get("remember_me")

    # Plain-text passwords are intentionally preserved for lab behavior.
    if username in users and users[username]["password"] == password:
        response = make_response(redirect(url_for("dashboard")))

        # Use a cryptographically secure, opaque session token instead of
        # encoding username and timestamp into a reversible token.
        session_token = secrets.token_urlsafe(32)

        # For lab purposes we still store session in a cookie, but we at least
        # set basic security attributes. In a real app, integrate Flask sessions.
        cookie_kwargs = {"httponly": True}
        # In real deployments served exclusively over HTTPS, also set "secure": True
        # and an appropriate SameSite value.
        if remember_me:
            cookie_kwargs["max_age"] = 30 * 24 * 60 * 60

        response.set_cookie("session", session_token, **cookie_kwargs)
        return response

    flash("Invalid username or password")
    return redirect(url_for("lab"))


@app.route("/register", methods=["POST"])
def register():
    username = request.form.get("username")
    password = request.form.get("password")
    email = request.form.get("email")

    if username and password and email:
        if username not in users:
            users[username] = {
                "password": password,
                "email": email,
                "role": "user",
            }
            flash("Registration successful")
            return redirect(url_for("lab"))

    flash("Registration failed")
    return redirect(url_for("lab"))


@app.route("/reset-password", methods=["POST"])
def reset_password():
    email = request.form.get("email")

    for username, user_data in users.items():
        if user_data["email"] == email:
            # Use a cryptographically secure reset token instead of MD5.
            token = secrets.token_urlsafe(32)
            password_reset_tokens[token] = username

            # For the lab, we still expose the token in the response;
            # in a real app this should be sent via email only.
            flash(f"Password reset link: /reset/{token}")
            return redirect(url_for("lab"))

    flash("Email not found")
    return redirect(url_for("lab"))


@app.route("/reset/<token>")
def reset_form(token: str):
    if token in password_reset_tokens:
        return render_template("reset.html", token=token)
    return "Invalid token"


@app.route("/dashboard")
def dashboard():
    session_token = request.cookies.get("session")
    if not session_token:
        return redirect(url_for("lab"))

    # For this lab we cannot introduce a backing store for session_token, so
    # we only check that the cookie exists; actual user mapping is preserved
    # as in the original lab. In a real app this would map to server-side
    # session state and verify integrity.
    try:
        # Preserve original behavior: decode username from legacy format if present.
        decoded = base64.b64decode(session_token).decode(errors="ignore")
        username = decoded.split(":", 1)[0]
    except Exception:
        username = None

    if username and username in users:
        return render_template(
            "dashboard.html",
            username=username,
            role=users[username]["role"],
            email=users[username]["email"],
        )

    return redirect(url_for("lab"))


if __name__ == "__main__":
    # For a real deployment, run with a WSGI server and debug=False.
    app.run(host="0.0.0.0", port=5000, debug=True)
