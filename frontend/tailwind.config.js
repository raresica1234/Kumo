/** @type {import('tailwindcss').Config} */
import colors from "tailwindcss/colors";

module.exports = {
  mode: "jit",
  purge: ["./src/**/*.{js,scss,html}", "/src/index.html"],
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        primary: colors.blue[600],
        secondary: colors.red[600],
        error: colors.red[400],
        success: colors.green[700],
        elevation_starter: "var(--color-elevation-starter)",
      },
    },
    screens: {
      sm: { min: "0px", max: "674px" },
      md: { min: "675px", max: "1079px" },
      lg: { min: "1080px", max: "1439px" },
      xl: { min: "1440px", max: "1919px" },
      "2xl": { min: "1920px" },
    },
  },
  plugins: [],
  darkMode: "class",
};
