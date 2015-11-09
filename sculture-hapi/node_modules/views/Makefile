test:
	@./node_modules/.bin/mocha \
		--reporter spec

bench: install-bench bench-caching

check =										\
	if [ -z `which siege` ]; then						\
		echo "please install siege. http://www.joedog.org/siege-home/";	\
		exit 1;								\
	fi

install-bench:
	@$(call check)
	@npm install
	@cd bench; npm install

bench-caching: install-bench
	@./node_modules/.bin/benchmarx \
	  --title "views caching" \
		--runner siege \
		bench/*.js

.PHONY: test bench
