# Deploying the Goof app to Heroku

## Step-by-step tutorial

1. Sign-up to a free plan at heroku (https://www.heroku.com)
2. Install the `heroku` CLI. If you're on a macOS:

  ```
  brew install heroku/brew/heroku
  ```

3. Authenticate to Heroku and follow the instructions with the following command:

  ```
  heroku login
  ```
 
  
4. Change directories to the goof/ app repository you cloned/forked
5. Create a heroku app:

  ```
  heroku create
  ```
  
6. You'll have to update a Credit Card number at https://heroku.com/verify in order to enable the MongoDB addon next.

7. Add the MongoDB addon:

```
heroku addons:create mongolab:sandbox
```

9. Push the Goof app to be deployed on the heroku platform

```
git push heroku master
```

## Trouble-shooting

- If you need to inspect the logs

```
heroku logs --tail
```

- If you need to manually restart the deployed app:

```
heroku ps:scale web=1
```
