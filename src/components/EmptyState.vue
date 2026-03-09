<template>
  <section class="card empty-state">
    <span class="material-symbols-outlined">search_off</span>
    <h3>{{ title }}</h3>
    <p>{{ description }}</p>
    <button v-if="actionText" class="btn btn-primary" @click="$emit('action')">{{ actionText }}</button>
  </section>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: "暂无数据"
  },
  description: {
    type: String,
    default: "请稍后再试"
  },
  actionText: {
    type: String,
    default: ""
  }
});

defineEmits(["action"]);
</script>

<style scoped>
.empty-state {
  padding: 34px 20px;
  text-align: center;
}

.material-symbols-outlined {
  font-size: 48px;
  color: var(--muted);
}

h3 {
  margin: 8px 0 4px;
}

p {
  margin: 0 0 12px;
  color: var(--muted);
}
</style>
