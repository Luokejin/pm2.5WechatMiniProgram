const app = getApp();
const config = app.config;
const lab = require('../../lib/lab');

Page({
  data: {
    pm25:'',
    api:'',
    level:'',
    good1:'',
    good2:'',
    bad1:'',
    bad2:''

  },
  onLoad: function () {
    var that = this;
    wx.request({
      url: 'https://www.fosulkj.cn/data',
      headers: {
        'Content-Type': 'application/json'
      },
      method:'POST',
      success: function (res) {
        //将获取到的json数据，存在名字叫zhihu的这个数组中
        console.log(res.data.pm25);
        that.data.pm25 = res.data.pm25;
        //最低浓度限值
        var cl = [ 0, 15.5, 40.5, 65.5, 150.5, 250.5, 350.5];
        //最高浓度限值
        var ch = [ 15.4, 40.4, 65.4, 150.4, 250.4, 350.4, 500.4];

        //最低aqi限值
        var il = [0, 51, 101, 151, 201, 301, 401];
        //最高aqi限值
        var ih = [50, 100, 150, 200, 300, 400 ,500];

        //程度
        var level = ['良好','中等','轻度污染','中度污染','重度污染','极度污染','不存在的'];
        //建议“宜”
        var good1 = ['游泳健身', '游泳健身','佛系少年','佛系少年','一首凉凉','一首凉凉','一首凉凉'];
        var good2 = ['了解一下', '了解一下', '修身养性', '修身养性', '赠送给你', '赠送给你', '赠送给你'];
        //建议“忌”
        var bad1 = ['大吉大利', '大吉大利','外出试毒','放弃治疗','放弃治疗','放弃治疗','放弃治疗'];
        var bad2 = ['今晚吃鸡', '今晚吃鸡', '舍命锻炼', '沉迷跑毒', '放飞自我', '放飞自我', '放飞自我'];

        //判断值
        for(var i = 0 ; i < 7; i++ ){
          if( that.data.pm25 > cl[i] && that.data.pm25 < ch[i] ){
            that.data.aqi = Math.floor(((ih[i]-il[i])/(ch[i]-cl[i])*(that.data.pm25-cl[i])) + il[i]);
            that.data.level = level[i];
            that.data.good1 = good1[i];
            that.data.bad1 = bad1[i];
            that.data.good2 = good2[i];
            that.data.bad2 = bad2[i];
          }
        }
        console.log(Math.floor(that.data.aqi));

        that.setData({
          pm25: that.data.pm25,
          level:that.data.level,
          aqi:that.data.aqi,
          good1: that.data.good1,
          good2: that.data.good2,
          bad1: that.data.bad1 ,
          bad2: that.data.bad2
          

        })
      }
    });
    setInterval(function () {
      wx.request({
        url: 'https://www.fosulkj.cn/data',
        headers: {
          'Content-Type': 'application/json'
        },
        success: function (res) {
          //将获取到的json数据，存在名字叫zhihu的这个数组中
          console.log(res.data.pm25);
          that.data.pm25 = res.data.pm25;
          //最低浓度限值
          var cl = [0, 15.5, 40.5, 65.5, 150.5, 250.5, 350.5];
          //最高浓度限值
          var ch = [15.4, 40.4, 65.4, 150.4, 250.4, 350.4, 500.4];

          //最低aqi限值
          var il = [0, 51, 101, 151, 201, 301, 401];
          //最高aqi限值
          var ih = [50, 100, 150, 200, 300, 400, 500];

          //程度
          var level = ['良好', '中等', '轻度污染', '中度污染', '重度污染', '极度污染', '不存在的'];
          //建议“宜”
          var good1 = ['拒绝肥宅', '拒绝肥宅', '佛系少年', '佛系少年', '一首凉凉', '一首凉凉', '一首凉凉'];
          var good2 = ['外出偶遇', '外出偶遇', '修身养性', '修身养性', '赠送给你', '赠送给你', '赠送给你'];
          //建议“忌”
          var bad1 = ['假装学习', '假装学习', '外出试毒', '放弃治疗', '放弃治疗', '放弃治疗', '放弃治疗'];
          var bad2 = ['心系手机', '心系手机', '舍命锻炼', '沉迷跑毒', '放飞自我', '放飞自我', '放飞自我'];

          //判断值
          for (var i = 0; i < 7; i++) {
            if (that.data.pm25 > cl[i] && that.data.pm25 < ch[i]) {
              that.data.aqi = Math.floor(((ih[i] - il[i]) / (ch[i] - cl[i]) * (that.data.pm25 - cl[i])) + il[i]);
              that.data.level = level[i];
              
            }
          }
          console.log(Math.floor(that.data.aqi));

          that.setData({
            pm25: that.data.pm25,
            level: that.data.level,
            aqi: that.data.aqi

          })
        }
      })
    }, 3000);


  }
})

