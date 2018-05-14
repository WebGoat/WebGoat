## Release WebGoat


### Version numbers

For WebGoat we use milestone releases first before we release the official version, we use `v8.0.0.M3` while tagging
 and 8.0.0.M3 in the `pom.xml`. When we create the final release we remove the milestone release and use 
 `v8.0.0` and 8.0.0 in the `pom.xml`

At the moment we use Gitflow, for a release you create a new release branch and take the following steps:

```
git checkout develop
git flow release start <version>
mvn versions:set <<version> 
git commit -am "New release, updaing pom.xml" 
git flow release publish
```

Now we can make a new release, be sure you committed all your changes.

```
git tag v8.0.0.M3
git push origin v8.0.0.M3 
```

Now Travis takes over and will create the release in Github and on Docker Hub. 


