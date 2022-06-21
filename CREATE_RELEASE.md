## Release WebGoat


### Version numbers

For WebGoat we use milestone releases first before we release the official version, we use `v8.0.0.M3` while tagging
 and 8.0.0.M3 in the `pom.xml`. When we create the final release we remove the milestone release and use 
 `v8.0.0` in the `pom.xml`
 
### Release notes:
Update the release notes with the correct version. Use `git shortlog -s -n --since "SEP 31 2019"` for the list of 
committers.

At the moment we use Gitflow, for a release you create a new release branch and take the following steps:

```
git checkout develop
git flow release start <version> 
git flow release publish

<<Make changes if necessary>>
<<Update RELEASE_NOTES.md>>

git flow release finish <version>
git push origin develop
git push origin main
git push --tags
```

Now Travis takes over and will create the release in Github and on Docker Hub.

NOTE: the `mvn versions:set` command above is just there to make sure the master branch contains the latest version


