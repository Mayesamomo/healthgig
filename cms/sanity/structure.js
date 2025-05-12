// ./structure.js
import {FaCog, FaNewspaper, FaFile, FaQuestionCircle, FaComment, FaUser} from 'react-icons/fa'

export default (S) =>
  S.list()
    .title('Content')
    .items([
      S.listItem()
        .title('Site Settings')
        .icon(FaCog)
        .child(
          S.editor()
            .id('siteSettings')
            .schemaType('siteSettings')
            .documentId('siteSettings')
        ),
      S.divider(),
      S.listItem()
        .title('Blog Posts')
        .icon(FaNewspaper)
        .schemaType('blogPost')
        .child(S.documentTypeList('blogPost').title('Blog Posts')),
      S.listItem()
        .title('Pages')
        .icon(FaFile)
        .schemaType('page')
        .child(S.documentTypeList('page').title('Pages')),
      S.listItem()
        .title('Authors')
        .icon(FaUser)
        .schemaType('author')
        .child(S.documentTypeList('author').title('Authors')),
      S.divider(),
      S.listItem()
        .title('Categories')
        .schemaType('category')
        .child(S.documentTypeList('category').title('Categories')),
      S.listItem()
        .title('FAQs')
        .icon(FaQuestionCircle)
        .schemaType('faq')
        .child(S.documentTypeList('faq').title('FAQs')),
      S.listItem()
        .title('Testimonials')
        .icon(FaComment)
        .schemaType('testimonial')
        .child(S.documentTypeList('testimonial').title('Testimonials')),
    ])