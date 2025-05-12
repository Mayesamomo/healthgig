export default {
  name: 'page',
  title: 'Page',
  type: 'document',
  fields: [
    {
      name: 'title',
      title: 'Title',
      type: 'string',
      validation: Rule => Rule.required(),
    },
    {
      name: 'slug',
      title: 'Slug',
      type: 'slug',
      options: {
        source: 'title',
        maxLength: 96,
      },
      validation: Rule => Rule.required(),
    },
    {
      name: 'pageType',
      title: 'Page Type',
      type: 'string',
      options: {
        list: [
          {title: 'Standard Page', value: 'standard'},
          {title: 'Legal Page', value: 'legal'},
          {title: 'FAQ Page', value: 'faq'},
          {title: 'About Page', value: 'about'},
          {title: 'Contact Page', value: 'contact'},
        ],
      },
      validation: Rule => Rule.required(),
    },
    {
      name: 'isPublished',
      title: 'Published',
      type: 'boolean',
      description: 'Set to published when this page should be visible to users',
    },
    {
      name: 'publishedAt',
      title: 'Published at',
      type: 'datetime',
      hidden: ({document}) => !document?.isPublished,
    },
    {
      name: 'mainImage',
      title: 'Main image',
      type: 'image',
      options: {
        hotspot: true,
      },
    },
    {
      name: 'excerpt',
      title: 'Excerpt',
      type: 'text',
      rows: 3,
      description: 'Short description of the page content',
    },
    {
      name: 'content',
      title: 'Page Content',
      type: 'array',
      of: [
        {
          type: 'block',
        },
        {
          type: 'image',
          options: {
            hotspot: true,
          },
          fields: [
            {
              name: 'caption',
              type: 'string',
              title: 'Caption',
              options: {
                isHighlighted: true,
              },
            },
            {
              name: 'alt',
              type: 'string',
              title: 'Alternative text',
              description: 'Important for SEO and accessibility',
              options: {
                isHighlighted: true,
              },
            },
          ],
        },
      ],
    },
    {
      name: 'faqItems',
      title: 'FAQ Items',
      type: 'array',
      of: [{type: 'reference', to: {type: 'faq'}}],
      hidden: ({document}) => document?.pageType !== 'faq',
    },
    {
      name: 'testimonials',
      title: 'Testimonials',
      type: 'array',
      of: [{type: 'reference', to: {type: 'testimonial'}}],
      hidden: ({document}) => document?.pageType !== 'about',
    },
    {
      name: 'seo',
      title: 'SEO',
      type: 'object',
      fields: [
        {
          name: 'metaTitle',
          title: 'Meta Title',
          type: 'string',
        },
        {
          name: 'metaDescription',
          title: 'Meta Description',
          type: 'text',
          rows: 3,
        },
        {
          name: 'keywords',
          title: 'Keywords',
          type: 'array',
          of: [{type: 'string'}],
          options: {
            layout: 'tags',
          },
        },
      ],
    },
  ],
  preview: {
    select: {
      title: 'title',
      media: 'mainImage',
      pageType: 'pageType',
    },
    prepare(selection) {
      const {pageType} = selection
      return {...selection, subtitle: pageType && `${pageType} page`}
    },
  },
}