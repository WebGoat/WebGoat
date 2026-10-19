module.exports = {
  name: "Users",
  columns: {
    id: {
      primary: true,
      type: "int",
      generated: true
    },
    name: {
      type: "varchar"
    },
    address: {
      type: "varchar"
    },
    role: {
      type: "varchar"
    }
  }
};