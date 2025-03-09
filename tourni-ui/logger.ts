import log from 'loglevel';

// Set the default log level
log.setDefaultLevel(log.levels.INFO);

// Create a logger instance
const logger = log.getLogger('tourni-ui');

// Set the log level for the logger instance
logger.setLevel(log.levels.WARN);

export default logger;
