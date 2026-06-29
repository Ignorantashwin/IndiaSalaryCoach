import { motion } from 'framer-motion';

interface CardProps {
  children: React.ReactNode;
  className?: string;
  hover?: boolean;
}

export function Card({ children, className = '', hover = false }: CardProps) {
  const Component = hover ? motion.div : 'div';
  const hoverProps = hover
    ? { whileHover: { y: -2, boxShadow: '0 10px 40px -10px rgba(0,0,0,0.1)' } }
    : {};

  return (
    <Component
      className={`bg-white dark:bg-gray-900 rounded-xl border border-gray-200 dark:border-gray-800 shadow-sm ${className}`}
      {...hoverProps}
    >
      {children}
    </Component>
  );
}
