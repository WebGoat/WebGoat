# Contributing

[![GitHub contributors](https://img.shields.io/github/contributors/WebGoat/WebGoat.svg)](https://github.com/WebGoat/WebGoat/graphs/contributors)
![GitHub issues by-label "help wanted"](https://img.shields.io/github/issues/WebGoat/WebGoat/help%20wanted.svg)
![GitHub issues by-label "good first issue"](https://img.shields.io/github/issues/WebGoat/WebGoat/good%20first%20issue.svg)
[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-%23FE5196?logo=conventionalcommits&logoColor=white)](https://conventionalcommits.org)

This document describes how you can contribute to WebGoat. Please read it carefully.

**Table of Contents**

* [How to Contribute to the Project](#how-to-contribute-to-the-project)
* [How to set up your Contributor Environment](#how-to-set-up-your-contributor-environment)
* [How to get your PR Accepted](#how-to-get-your-pr-accepted)

## How to Contribute to the project

There are a couple of ways on how you can contribute to the project:

* **File [issues](https://github.com/WebGoat/WebGoat/issues "Webgoat Issues")** for missing content or errors. Explain what you think is missing and give a suggestion as to where it could be added.
* **Create a [pull request (PR)](https://github.com/WebGoat/WebGoat/pulls "Create a pull request")**. This is a direct contribution to the project and may be merged after review. You should ideally [create an issue](https://github.com/WebGoat/WebGoat/issues "WebGoat Issues") for any PR you would like to submit, as we can first review the merit of the PR and avoid any unnecessary work. This is of course not needed for small modifications such as correcting typos.
* **Help out financially** by donating via [OWASP donations](https://owasp.org/donate/?reponame=www-project-webgoat&title=OWASP+WebGoat).

## How to get your PR accepted

Your PR is valuable to us, and to make sure we can integrate it smoothly, we have a few items for you to consider. In short:
The minimum requirements for code contributions are:

1. The code _must_ be compliant with the configured Java Google Formatter, Checkstyle and PMD rules.
2. All new and changed code _should_ have a corresponding unit and/or integration test.
3. New and changed lessons _must_ have a corresponding integration test.
4. [Status checks](https://docs.github.com/en/github/collaborating-with-pull-requests/collaborating-on-repositories-with-code-quality-features/about-status-checks) should pass for your last commit.

Additionally, the following guidelines can help:

### Keep your pull requests limited to a single issue

Pull requests should be as small/atomic as possible. Large, wide-sweeping changes in a pull request will be **rejected**, with comments to isolate the specific code in your pull request. Some examples:

* If you are making spelling corrections in the docs, don't modify other files.
* If you are adding new functions don't '*cleanup*' unrelated functions. That cleanup belongs in another pull request.

### Write a good commit message

* We use [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) and use the following types:

  - fix:
  - feat:
  - build:
  - chore:
  - ci:
  - docs:
  - refactor:
  - test:

  Using this style of commits makes it possible to create our release notes automatically.

* Explain why you make the changes. [More infos about a good commit message.](https://betterprogramming.pub/stop-writing-bad-commit-messages-8df79517177d)

* If you fix an issue with your commit, please close the issue by [adding one of the keywords and the issue number](https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue) to your commit message.

For example: `Fix #545` or `Closes #10`

## How to set up your Contributor Environment

1. Create a GitHub account. Multiple different GitHub subscription plans are available, but you only need a free one. Follow [these steps](https://help.github.com/en/articles/signing-up-for-a-new-github-account "Signing up for a new GitHub account") to set up your account.
2. Fork the repository. Creating a fork means creating a copy of the repository on your own account, which you can modify without any impact on this repository. GitHub has an [article that describes all the needed steps](https://help.github.com/en/articles/fork-a-repo "Fork a repo").
3. Clone your own repository to your host computer so that you can make modifications. If you followed the GitHub tutorial from step 2, you have already done this.
4. Go to the newly cloned directory "WebGoat" and add the remote upstream repository:

   ```bash
   	$ git remote -v
   	origin git@github.com:<your Github handle>/WebGoat.git (fetch)
   	origin git@github.com:<your Github handle>/WebGoat.git (push)

   	$ git remote add upstream git@github.com:WebGoat/WebGoat.git

   	$ git remote -v
   	origin git@github.com:<your Github handle>/WebGoat.git (fetch)
   	origin git@github.com:<your Github handle>/WebGoat.git (push)
   	upstream git@github.com:OWASP/WebGoat.git (fetch)
   	upstream git@github.com:OWASP/WebGoat.git (push)
   ```

   See also the GitHub documentation on "[Configuring a remote for a fork](https://docs.github.com/en/free-pro-team@latest/github/collaborating-with-issues-and-pull-requests/configuring-a-remote-for-a-fork "Configuring a remote for a fork")".

5. Choose what to work on, based on any of the outstanding [issues](https://github.com/WebGoat/WebGoat/issues "WebGoat Issues").

6. Create a branch so that you can cleanly work on the chosen issue: `git checkout -b FixingIssue66`

7. Open your favorite editor and start making modifications. We recommend using the [IntelliJ Idea](https://www.jetbrains.com/idea/).

8. After your modifications are done, push them to your forked repository. This can be done by executing the command `git add MYFILE` for every file you have modified, followed by `git commit -m 'your commit message here'` to commit the modifications and `git push` to push your modifications to GitHub.

9. Create a Pull Request (PR) by going to your fork, <https://github.com/Your_Github_Handle/WebGoat> and click on the "New Pull Request" button. The target branch should typically be the Master branch. When submitting a PR, be sure to follow the checklist that is provided in the PR template. The checklist itself will be filled out by the reviewer.

10. Your PR will be reviewed and comments may be given. In order to process a comment, simply make modifications to the same branch as before and push them to your repository. GitHub will automatically detect these changes and add them to your existing PR.

11. When starting on a new PR in the future, make sure to always keep your local repo up to date:

    ```bash
    $ git fetch upstream
    $ git merge upstream/main
    ```

    See also the following article for further explanation on "[How to Keep a Downstream git Repository Current with Upstream Repository Changes](https://medium.com/sweetmeat/how-to-keep-a-downstream-git-repository-current-with-upstream-repository-changes-10b76fad6d97 "How to Keep a Downstream git Repository Current with Upstream Repository Changes")".

If at any time you want to work on a different issue, you can simply switch to a different branch, as explained in step 5.

> Tip: Don't try to work on too many issues at once though, as it will be a lot more difficult to merge branches the longer they are open.

## What not to do

Although we greatly appreciate any and all contributions to the project, there are a few things that you should take into consideration:

* The WebGoat project should not be used as a platform for advertisement for commercial tools, companies or individuals. Write-ups should be written with free and open-source tools in mind and commercial tools are typically not accepted, unless as a reference in the security tools section.
* Unnecessary self-promotion of tools or blog posts is frowned upon. If you have a relation with on of the URLs or tools you are referencing, please state so in the PR so that we can verify that the reference is in line with the rest of the guide.

Please be sure to take a careful look at our [Code of Conduct](https://github.com/WebGoat/WebGoat/blob/master/CODE_OF_CONDUCT.md) for all the details.
