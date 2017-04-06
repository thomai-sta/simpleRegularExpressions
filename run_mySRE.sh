#!/bin/sh
java -cp classes mySRE.Testing "(A*B|ac)d" "a" "aaa" "" "ac" "ab" "aaaac" "b" "c" "abbcb" "abbcbacb" "abcbcd" "abcbcbcdaaaabcbcdaaaddd" "AAAA" "AAAAC" "aaabd"
