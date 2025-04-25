<template>
  <Card title="项目" v-bind="$attrs">
    <template #extra>
      <a-button type="link" size="small" @click="more">更多</a-button>
    </template>

    <template v-for="item in items" :key="item">
      <CardGrid class="!md:w-1/3 !w-full" @click="linkProject(item)">
        <span class="flex">
          <Icon :icon="item.icon" :color="item.color" size="30" />
          <span class="text-lg ml-4">{{ item.title }}</span>
        </span>
        <div class="flex mt-2 h-10 text-secondary"> {{ item.desc }}</div>
        <div class="flex justify-between text-secondary">
          <span>{{ item.group }}</span>
          <span>{{ item.date }}</span>
        </div>
      </CardGrid>
    </template>
  </Card>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Card } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import { groupItems } from './data';
  import { useRouter } from 'vue-router';

  export default defineComponent({
    components: { Card, CardGrid: Card.Grid, Icon },
    setup() {
      const router = useRouter();
      const more = () => {
        router.push('/dashboard/workbench/projectMore');
      };
      const linkProject = (item) => {
        if (item.isExternalLink) {
          // 外链
          window.open(item.url);
        } else {
          // 内链
          router.push(item.url);
        }
      };
      return { items: groupItems, more, linkProject };
    },
  });
</script>
