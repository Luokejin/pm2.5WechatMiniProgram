//数据模型层
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

mongoose.connect('mongodb://localhost:27017/webapp',{useMongoClient: true});
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function (callback) {
  console.log('db service connected.')
});

var dataSchema = new mongoose.Schema({
    time: String,
	title:String,
	pm25:Number
});


module.exports = mongoose.model('data', dataSchema);