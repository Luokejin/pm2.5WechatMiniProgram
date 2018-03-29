var dataSchema = require('./data');
module.exports.data = {
	//GET访问的/data路由访问
	get: function (req, res, next){
	
		dataSchema.findOne({ "title":"MongoDB" },function(err,json){
            if(err) console.log(err);
			res.send(JSON.stringify(json));
        });
	}
};