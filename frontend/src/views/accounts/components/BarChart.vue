<template>
  <Bar
    :chart-options="chartOptions"
    :chart-data="{
        labels: mbtiLabel,
        datasets: [
          {
            label: 'MBTI',
            backgroundColor: '#D5ACD5',
            data: mbtiRate
          }
        ]
      }"
    :chart-id="chartId"
    :dataset-id-key="datasetIdKey"
    :plugins="plugins"
    :css-classes="cssClasses"
    :styles="styles"
    :width="width"
    :height="height"
  />
</template>

<script>
import { Bar } from 'vue-chartjs/legacy'

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
import { mapActions, mapGetters } from 'vuex'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
  name: 'BarChart',
  components: {
    Bar
  },
  props: {
    chartId: {
      type: String,
      default: 'bar-chart'
    },
    datasetIdKey: {
      type: String,
      default: 'label'
    },
    width: {
      type: Number,
      default: 400
    },
    height: {
      type: Number,
      default: 400
    },
    cssClasses: {
      default: '',
      type: String
    },
    styles: {
      type: Object,
      default: () => {}
    },
    plugins: {
      type: Array,
      default: () => []
    }
  },
  data () {
    return {
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false
      }
    }
  },
  created () {
    this.getmbti()
  },
  methods: {
    ...mapActions(['getmbti'])
  },
  computed: {
    ...mapGetters(['mbtiRate', 'mbtiLabel'])
  }
}
</script>
