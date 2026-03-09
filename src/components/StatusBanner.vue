<template>
  <div v-if="visible" class="status-banner">
    <span class="material-symbols-outlined">info</span>
    当前为演示数据模式（部分接口不可用，已自动回退到 mock 数据）
  </div>
</template>

<script setup>
defineProps({
  visible: {
    type: Boolean,
    default: false
  }
});
</script>

<style scoped>
.status-banner {
  width: min(1180px, calc(100% - 32px));
  margin: 14px auto 0;
  color: #7a4c00;
  background: #fff4e8;
  border: 1px solid #ffd9b4;
  border-radius: 12px;
  padding: 10px 12px;
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 600;
}

@media (max-width: 900px) {
  .status-banner {
    width: min(100% - 20px, 100%);
  }
}
</style>
