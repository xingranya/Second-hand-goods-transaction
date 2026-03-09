<template>
  <aside class="card list-wrap">
    <h3>消息会话</h3>
    <button
      v-for="item in conversations"
      :key="item.id"
      class="conversation"
      :class="{ active: activeId === item.id }"
      @click="$emit('select', item.id)"
    >
      <div>
        <strong>{{ item.peerUser?.name || "未知用户" }}</strong>
        <p>{{ item.lastMessage }}</p>
      </div>
      <span v-if="item.unreadCount" class="badge">{{ item.unreadCount }}</span>
    </button>
  </aside>
</template>

<script setup>
defineProps({
  conversations: {
    type: Array,
    default: () => []
  },
  activeId: {
    type: String,
    default: ""
  }
});

defineEmits(["select"]);
</script>

<style scoped>
.list-wrap {
  padding: 12px;
}

h3 {
  margin: 4px 0 12px;
}

.conversation {
  width: 100%;
  text-align: left;
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 12px;
  margin-bottom: 10px;
  background: var(--surface);
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
}

.conversation p {
  margin: 6px 0 0;
  color: var(--muted);
  font-size: 12px;
}

.conversation.active {
  border-color: var(--primary);
  background: #edf3ff;
}

.badge {
  min-width: 20px;
  height: 20px;
  border-radius: 999px;
  background: var(--primary);
  color: #fff;
  font-size: 12px;
  display: grid;
  place-items: center;
}
</style>
