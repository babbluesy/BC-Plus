<template>
  <div id="q-app">
    <link src="@/assets/favicon/favicon.ico"/>
    <router-view :key="$route.fullPath"/>
  </div>
</template>

<script>

export default {

}
</script>

<style lang="scss">
html {
  scroll-behavior: smooth !important;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}

.icon-btn {
  cursor: pointer;
}

/* width */
// ::-webkit-scrollbar {
//   width: 5px;
// }

// /* Track */
// ::-webkit-scrollbar-track {
//   background: transparent;
// }

// /* Handle */
// ::-webkit-scrollbar-thumb {
//   background: #888;
// }

// /* Handle on hover */
// ::-webkit-scrollbar-thumb:hover {
//   background: #555;
// }

body.body--dark {
  background: #131E2E;
  color: #F3F3F3;
}

// .dark_border {
//   border: 1px solid black !important;
// }
</style>