export default {
  name: 'testimonial',
  title: 'Testimonial',
  type: 'document',
  fields: [
    {
      name: 'name',
      title: 'Name',
      type: 'string',
      validation: Rule => Rule.required(),
    },
    {
      name: 'role',
      title: 'Role Type',
      type: 'string',
      options: {
        list: [
          {title: 'Healthcare Worker', value: 'worker'},
          {title: 'Healthcare Facility', value: 'facility'},
        ],
      },
      validation: Rule => Rule.required(),
    },
    {
      name: 'title',
      title: 'Professional Title/Facility Type',
      type: 'string',
      description: 'E.g., "Registered Nurse" or "Urgent Care Clinic"',
    },
    {
      name: 'image',
      title: 'Image',
      type: 'image',
      options: {
        hotspot: true,
      },
    },
    {
      name: 'quote',
      title: 'Quote',
      type: 'text',
      rows: 4,
      validation: Rule => Rule.required(),
    },
    {
      name: 'rating',
      title: 'Rating (1-5)',
      type: 'number',
      validation: Rule => Rule.required().min(1).max(5),
    },
    {
      name: 'featured',
      title: 'Featured',
      type: 'boolean',
      description: 'Set to true to highlight this testimonial on the homepage',
    },
    {
      name: 'order',
      title: 'Order',
      type: 'number',
      description: 'Display order (lower numbers appear first)',
    },
  ],
  preview: {
    select: {
      title: 'name',
      media: 'image',
      subtitle: 'title',
      rating: 'rating',
    },
    prepare({title, media, subtitle, rating}) {
      return {
        title,
        media,
        subtitle: subtitle ? `${subtitle} - ${rating} ★` : `${rating} ★`,
      }
    },
  },
}