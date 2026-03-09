<template>
  <aside class="card filter-panel">
    <h3>筛选条件</h3>
    <label>
      <span>关键词</span>
      <input class="input" :value="modelValue.keyword" @input="emitChange('keyword', $event.target.value)" />
    </label>
    <label>
      <span>校区</span>
      <select class="input" :value="modelValue.campus" @change="emitChange('campus', $event.target.value)">
        <option value="">全部校区</option>
        <option value="主校区">主校区</option>
        <option value="东校区">东校区</option>
        <option value="西校区">西校区</option>
      </select>
    </label>
    <label>
      <span>价格排序</span>
      <select class="input" :value="modelValue.sort" @change="emitChange('sort', $event.target.value)">
        <option value="latest">最新发布</option>
        <option value="priceAsc">价格从低到高</option>
        <option value="priceDesc">价格从高到低</option>
      </select>
    </label>
    <button class="btn btn-primary" @click="$emit('apply')">应用筛选</button>
  </aside>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(["update:modelValue", "apply"]);

function emitChange(key, value) {
  emit("update:modelValue", {
    ...props.modelValue,
    [key]: value
  });
}
</script>

<style scoped>
.filter-panel {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

h3 {
  margin: 0 0 6px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 13px;
  color: var(--muted);
}
</style>
