<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 60px">
      <el-col :span="6">
        <el-card>
          <div style="color: #409EFF"><i class="el-icon-user"></i> 用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div style="color: #F56C6C"><i class="el-icon-money"></i> 销售总量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            ￥ 10000000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div style="color: #E6A23C"><i class="el-icon-bank-card"></i> 收益总量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            ￥ 300000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div style="color: #909399"><i class="el-icon-s-shop"></i> 门店总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            20
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="main" style="height: 380px; width: 500px"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="height: 400px; width: 500px"></div>
      </el-col>
    </el-row>
  </div>

</template>

<script>
    import * as echarts from 'echarts';

    export default {
        name: "Home",
        data(){
            return{

            }

        },
        mounted() {  //元素绚烂之后再触发
          const chartDom = document.getElementById('main');
          const myChart = echarts.init(chartDom);
          const option = {
                title: {
                    text: 'Referer of a Website',
                    subtext: 'Fake Data',
                    left: 'center'
                },
               tooltip: {
                    trigger: 'item'
               },
               legend: {
                    orient: 'vertical',
                    left: 'left'
               },
                xAxis: {
                    type: 'category',
                    data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                  {
                    name: '星巴克',
                    data: [],
                    type: 'line'
                  },
                  {
                    name: '星巴克',
                    data: [],
                    type: 'bar'
                  },
                  {
                    name: '瑞信',
                    data: [],
                    type: 'bar'
                  },
                  {
                    name: '瑞信',
                    data: [],
                    type: 'line'
                  },
                ]
            };
            this.request.get("http://localhost:9090/echarts/members").then(res => {
               /* option.xAxis.data = res.data.x
                console.log(option.xAxis.data)
                option.series[0].data = res.data.y
                console.log(option.series[0].data)
                option.series[1].data = res.data.y*/
                option.series[1].data = res.data
                option.series[0].data = res.data
                option.series[2].data = [90,80,70,60,50,40,60]
                option.series[3].data = [90,80,70,60,50,40,60]
                myChart.setOption(option);
            })



            //饼图
          const pieOption ={

                title: {
                    text: 'Referer of a Website',
                    subtext: 'Fake Data',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                series: [
                    {
                        name: 'Access From',
                        type: 'pie',
                        radius: '50%',
                       /* label: {            //饼图图形上的文本标签
                          normal: {
                            show: true,
                            position: 'inner', //标签的位置
                            textStyle: {
                              fontWeight: 300,
                              fontSize: 14,    //文字的字体大小
                              color: "#fff"
                            },
                            formatter: '{d}%'
                          }
                        },*/
                        data: [
                           /* { value: 1048, name: 'Search Engine' },
                            { value: 735, name: 'Direct' },
                            { value: 580, name: 'Email' },
                            { value: 484, name: 'Union Ads' },
                            { value: 300, name: 'Video Ads' }*/
                        ],
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };
          const pieDom = document.getElementById('pie');
          const pieChart = echarts.init(pieDom);
          this.request.get("http://localhost:9090/echarts/members").then(res => {
            pieOption.series[0].data = [
              { value: res.data[0], name: 'Search Engine' },
              { value: res.data[1], name: 'Direct' },
              { value: res.data[2], name: 'Email' },
              { value: res.data[3], name: 'Union Ads' },
              { value: res.data[7], name: 'Video Ads' },
            ]

            pieChart.setOption(pieOption);
          })

        },
    }
</script>

<style scoped>

</style>