module.exports = process.env.merge_COV
  ? require('./lib-cov/merge')
  : require('./lib/merge');
