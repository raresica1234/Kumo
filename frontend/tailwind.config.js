/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {},
    screens: {
      "sm": {min: "0px", max: "674px"},
      "md": {min: "675px", max: "1079px"},
      "lg": {min: "1080px", max: "1439px"},
      "xl": {min: "1440px", max: "1919px"},
      "2xl": {min: "1920px"}
    }
  },
  plugins: [],
  darkMode: "class"
}

