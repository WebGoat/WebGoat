# Working via curl

if [ -z "$GOOF_HOST" ]; then
	export GOOF_HOST=http://localhost:3001
fi

alias ms1="echo 'content=Call mom in 20 minutes' | http --form $GOOF_HOST/create -v"

# Works with long string that matches
alias ms2="echo 'content=Buy milk in '\`printf "%.0s5" {1..60000}\`' minutes' | http --form $GOOF_HOST/create -v"

# Hangs with long string that doesn't match
alias ms3="echo 'content=Buy milk in '\`printf "%.0s5" {1..60000}\`' minutea' | http --form $GOOF_HOST/create -v"
