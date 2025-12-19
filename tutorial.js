const express = require("express");
const {
    environmentalScripts
} = require("../../config/config");

const router = express.Router();

router.get("/", (req, res) => {
    "use strict";
    return res.render("tutorial/a1", {
        environmentalScripts
    });
});

const pages = [
    "a1",
    "a2",
    "a3",
    "a4",
    "a5",
    "a6",
    "a7",
    "a8",
    "a9",
    "a10",
    "redos",
    "ssrf"
];

for(const page of pages) {
    router.get(`/${page}`, (req, res) => {
        "use strict";
        return res.render(`tutorial/${page}`, {
            environmentalScripts
        });
    });
}

module.exports = router;
