var dataSchema = require('./data');
module.exports.data = {
	//GET���ʵ�/data·�ɷ���
	get: function (req, res, next){
	
		dataSchema.findOne({ "title":"MongoDB" },function(err,json){
            if(err) console.log(err);
			res.send(JSON.stringify(json));
        });
	}
};