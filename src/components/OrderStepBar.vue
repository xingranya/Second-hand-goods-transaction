<template>
  <div class="steps card">
    <div v-for="(step, index) in stepList" :key="step" class="step" :class="{ active: index <= activeIndex }">
      <span class="dot"></span>
      <span>{{ step }}</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  status: {
    type: String,
    default: "已下单"
  }
});

const stepList = ["已下单", "已付款", "已发货", "待收货", "待评价"];
const activeIndex = computed(() => Math.max(0, stepList.indexOf(props.status)));
</script>

<style scoped>
.steps {
  padding: 14px;
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 8px;
}

.step {
  color: var(--muted);
  font-size: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #cbd5e1;
}

.step.active {
  color: var(--primary);
  font-weight: 700;
}

.step.active .dot {
  background: var(--primary);
}
</style>
