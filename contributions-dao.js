const UserDAO = require("./user-dao").UserDAO;

/* The ContributionsDAO must be constructed with a connected database object */
function ContributionsDAO(db) {
    "use strict";

    /* If this constructor is called without the "new" operator, "this" points
     * to the global object. Log a warning and call it correctly. */
    if (false === (this instanceof ContributionsDAO)) {
        console.log("Warning: ContributionsDAO constructor called without 'new' operator");
        return new ContributionsDAO(db);
    }

    const contributionsDB = db.collection("contributions");
    const userDAO = new UserDAO(db);

    this.update = (userId, preTax, afterTax, roth, callback) => {
        const parsedUserId = parseInt(userId);

        // Create contributions document
        const contributions = {
            userId: parsedUserId,
            preTax: preTax,
            afterTax: afterTax,
            roth: roth
        };

        contributionsDB.update({
            userId
            },
            contributions, {
                upsert: true
            },
            err => {
                if (!err) {
                    console.log("Updated contributions");
                    // add user details
                    userDAO.getUserById(parsedUserId, (err, user) => {

                        if (err) return callback(err, null);

                        contributions.userName = user.userName;
                        contributions.firstName = user.firstName;
                        contributions.lastName = user.lastName;
                        contributions.userId = userId;

                        return callback(null, contributions);
                    });
                } else {
                    return callback(err, null);
                }
            }
        );
    };

    this.getByUserId = (userId, callback) => {
        contributionsDB.findOne({
                userId: userId
            },
            (err, contributions) => {
                if (err) return callback(err, null);

                // Set defualt contributions if not set
                contributions = contributions || {
                    preTax: 2,
                    afterTax: 2,
                    roth: 2
                };

                // add user details
                userDAO.getUserById(userId, (err, user) => {

                    if (err) return callback(err, null);
                    contributions.userName = user.userName;
                    contributions.firstName = user.firstName;
                    contributions.lastName = user.lastName;
                    contributions.userId = userId;

                    callback(null, contributions);
                });
            }
        );
    };
}

module.exports = { ContributionsDAO };
