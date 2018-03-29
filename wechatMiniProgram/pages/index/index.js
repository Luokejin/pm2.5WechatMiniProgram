const lab = require('../../lib/lab');

Page({
  data: {
    advert:[
      "关心环境，更关心你",
      "头上带点绿，生活过得去",
      "爱是一道光，如此的美妙",
      "绿色你我他，环境靠大家"
    ],
    location:'佛山'
  },
  onLoad: function () {
    var that = this;
    var advert = that.data.advert;
    var location = that.data.location;
    that.setData({
      advert: advert[Math.floor(Math.random() * advert.length)],
      location:location
    })
    console.log(advert[Math.floor(Math.random() * advert.length)]);
    // 引入SDK核心类
    var QQMapWX = require('../../lib/qqmap-wx-jssdk.js');

    // 实例化腾讯地图API核心类
    var qqmapsdk = new QQMapWX({
      key: 'F5WBZ-WM7KQ-IVB5Z-GN7OA-WI3HE-PNFHB' // 必填
    });
    //1、获取当前位置坐标
      wx.getLocation({
        type: 'wgs84',
        success: function (res) {
          //2、根据坐标获取当前位置名称，显示在顶部:腾讯地图逆地址解析
          qqmapsdk.reverseGeocoder({
            location: {
              latitude: res.latitude,
              longitude: res.longitude
            },
            success: function (addressRes) {
              var address = addressRes.result.formatted_addresses.recommend;
              console.log(address);
              that.setData({
               address: address
              })
            }
          })
        }
      })
  }

})