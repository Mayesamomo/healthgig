export default {
  name: 'faq',
  title: 'FAQ',
  type: 'document',
  fields: [
    {
      name: 'question',
      title: 'Question',
      type: 'string',
      validation: Rule => Rule.required(),
    },
    {
      name: 'answer',
      title: 'Answer',
      type: 'array',
      of: [{type: 'block'}],
      validation: Rule => Rule.required(),
    },
    {
      name: 'category',
      title: 'Category',
      type: 'string',
      options: {
        list: [
          {title: 'General', value: 'general'},
          {title: 'For Healthcare Workers', value: 'workers'},
          {title: 'For Healthcare Facilities', value: 'facilities'},
          {title: 'Payments & Billing', value: 'payments'},
          {title: 'Technical Support', value: 'support'},
          {title: 'Legal', value: 'legal'},
        ],
      },
    },
    {
      name: 'order',
      title: 'Order',
      type: 'number',
      description: 'Used to determine the display order within a category',
    },
  ],
  orderings: [
    {
      title: 'Category, Order',
      name: 'categoryAndOrderAsc',
      by: [
        {field: 'category', direction: 'asc'},
        {field: 'order', direction: 'asc'},
      ],
    },
  ],
  preview: {
    select: {
      title: 'question',
      subtitle: 'category',
    },
  },
}