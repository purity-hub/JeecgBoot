<template>
  <!--定义表格-->
  <BasicTable @register="registerTable">
    <template #tableTitle>
      <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate"> 新增</a-button>
    </template>
    <!--操作栏-->
    <template #action="{ record }">
      <TableAction :actions="getTableAction(record)" />
    </template>
  </BasicTable>
  <AiOcrModal @register="registerModal" @success="reload" />
</template>

<script lang="ts" name="basic-table-demo" setup>
  import { ActionItem, BasicColumn, BasicTable, TableAction } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { useModal } from '@/components/Modal';
  import AiOcrModal from '@/views/super/airag/ocr/AiOcrModal.vue';
  import { render } from '@/utils/common/renderUtils';
  import { deleteOcr, getOcrList } from '@/views/super/airag/ocr/ocr.api';
  //定义表格列字段
  const columns: BasicColumn[] = [
    {
      title: '标题(文件名)',
      dataIndex: 'fileName',
      key: 'fileName',
    },
    {
      title: '图片',
      dataIndex: 'imagePath',
      key: 'imagePath',
      customRender: render.renderImage,
    },
    {
      title: '识别结果',
      dataIndex: 'ocrResult',
      key: 'ocrResult',
    },
    {
      title: '上传时间',
      dataIndex: 'uploadTime',
      key: 'uploadTime',
    },
  ];
  // 通过hook useListPage渲染表格（设置dataSource、columns、actionColumn等参数）
  const { tableContext } = useListPage({
    designScope: 'basic-table-demo',
    tableProps: {
      title: 'OCR识别列表',
      api: getOcrList,
      columns: columns,
      size: 'small',
      actionColumn: {
        width: 180,
      },
    },
  });
  // BasicTable绑定注册
  const [registerTable, { reload, setLoading }, { rowSelection, selectedRowKeys, selectedRows }] = tableContext;
  const [registerModal, { openModal }] = useModal();
  /**
   * 操作栏
   */
  function getTableAction(record): ActionItem[] {
    return [
      {
        label: '解析',
        onClick: handleParse.bind(null, record),
      },
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '删除',
        color: 'error',
        popConfirm: {
          title: '确认要删除吗？',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }

  function handleCreate() {
    openModal(true, {
      title: '新增消息模板',
      isUpdate: false,
      record: {},
      showFooter: true,
    });
  }

  function handleParse(record) {
    openModal(true, {
      title: '解析',
      isUpdate: false,
      isParse: true,
      record: record,
      showFooter: true,
    });
  }

  function handleEdit(record) {
    openModal(true, {
      title: '修改消息模板',
      isUpdate: true,
      record: record,
      showFooter: true,
    });
  }

  function handleDelete(record) {
    setLoading(true);
    deleteOcr({ id: record.id }, () => {
      setLoading(false);
    }).finally(() => {
      setLoading(false);
      reload();
    });
  }
</script>
