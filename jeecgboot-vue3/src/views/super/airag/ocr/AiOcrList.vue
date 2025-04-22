<template>
  <!--定义表格-->
  <BasicTable @register="registerTable">
    <!--操作栏-->
    <template #action="{ record }">
      <TableAction :actions="getTableAction(record)" />
    </template>
  </BasicTable>
</template>

<script lang="ts" name="basic-table-demo" setup>
  import { ActionItem, BasicColumn, BasicTable, TableAction } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
  //定义表格列字段
  const columns: BasicColumn[] = [
    {
      title: '文件名',
      dataIndex: 'fileName',
      key: 'fileName',
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
      dataSource: [
        {
          key: '1',
          fileName: 'example1.jpg',
          ocrResult: '识别结果1',
          uploadTime: '2023-10-01 12:00:00',
        },
        {
          key: '2',
          fileName: 'example2.jpg',
          ocrResult: '识别结果2',
          uploadTime: '2023-10-02 12:00:00',
        },
      ],
      columns: columns,
      size: 'small',
      actionColumn: {
        width: 180,
      },
    },
  });
  // BasicTable绑定注册
  const [registerTable] = tableContext;
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
        onClick: handleDelete.bind(null, record),
      },
    ];
  }

  function handleParse(record) {
    console.log('解析:', record);
  }

  function handleEdit(record) {
    console.log('编辑:', record);
  }

  function handleDelete(record) {
    console.log('删除:', record);
  }
</script>

