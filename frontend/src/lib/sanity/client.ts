import { createClient } from 'next-sanity';
import imageUrlBuilder from '@sanity/image-url';
import { SanityImageSource } from '@sanity/image-url/lib/types/types';

export const config = {
  projectId: 'your-project-id', // Replace with your Sanity project ID
  dataset: 'production',
  apiVersion: '2023-05-03',
  useCdn: process.env.NODE_ENV === 'production',
};

export const sanityClient = createClient(config);

// Helper function for generating image URLs with the Sanity Image pipeline
const builder = imageUrlBuilder(sanityClient);

export function urlFor(source: SanityImageSource) {
  return builder.image(source);
}

// Fetch a specific page by slug
export async function getPageBySlug(slug: string) {
  return sanityClient.fetch(
    `*[_type == "page" && slug.current == $slug][0]{
      title,
      slug,
      pageType,
      mainImage,
      excerpt,
      content,
      seo,
      publishedAt,
      isPublished,
      "faqItems": faqItems[]->{
        _id,
        question,
        answer,
        category,
        order
      },
      "testimonials": testimonials[]->{
        _id,
        name,
        role,
        title,
        image,
        quote,
        rating,
        featured
      }
    }`,
    { slug }
  );
}

// Fetch all blog posts
export async function getAllBlogPosts() {
  return sanityClient.fetch(
    `*[_type == "blogPost"] | order(publishedAt desc){
      _id,
      title,
      slug,
      excerpt,
      mainImage,
      publishedAt,
      "categories": categories[]->title,
      "authorName": author->name,
      "authorImage": author->image
    }`
  );
}

// Fetch a specific blog post by slug
export async function getBlogPostBySlug(slug: string) {
  return sanityClient.fetch(
    `*[_type == "blogPost" && slug.current == $slug][0]{
      title,
      slug,
      body,
      mainImage,
      publishedAt,
      excerpt,
      "categories": categories[]->{
        _id,
        title,
        slug
      },
      "author": author->{
        name,
        slug,
        image,
        bio,
        title
      },
      seo
    }`,
    { slug }
  );
}

// Fetch all FAQs
export async function getAllFaqs() {
  return sanityClient.fetch(
    `*[_type == "faq"] | order(category asc, order asc){
      _id,
      question,
      answer,
      category,
      order
    }`
  );
}

// Fetch site settings
export async function getSiteSettings() {
  return sanityClient.fetch(`*[_type == "siteSettings"][0]`);
}

// Fetch all testimonials
export async function getAllTestimonials() {
  return sanityClient.fetch(
    `*[_type == "testimonial"] | order(order asc){
      _id,
      name,
      role,
      title,
      image,
      quote,
      rating,
      featured
    }`
  );
}

// Fetch only featured testimonials
export async function getFeaturedTestimonials() {
  return sanityClient.fetch(
    `*[_type == "testimonial" && featured == true] | order(order asc){
      _id,
      name,
      role,
      title,
      image,
      quote,
      rating
    }`
  );
}