## Release WebGoat

### Version numbers

For WebGoat we use milestone releases first before we release the official version, we use `v2023.01` while tagging
and 2023.01 in the `pom.xml`.

### Release notes:

Update the release notes with the correct version. Use `git shortlog -s -n --since "JAN 06 2023"` for the list of
committers.

```
mvn versions:set 
<< update release notes >>
mvn verify
git commit ....
git tag v2023.01 
git push --tags
```

