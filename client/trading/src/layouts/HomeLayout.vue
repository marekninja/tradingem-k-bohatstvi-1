<template>
  <q-layout view="hhh Lpr fff">

    <q-header bordered class="bg-primary text-white">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="home"
          aria-label="Menu"
          to='/'
        />
        <!-- @click="leftDrawerOpen = !leftDrawerOpen" -->

        <q-toolbar>
          <q-toolbar-title>
            Tradingem k bohatství
          </q-toolbar-title>
            <!-- <div class="text-h3 q-px-lg q-pt-xl q-mb-md">
              
            </div> -->
        </q-toolbar>
      </q-toolbar>
      <q-img src="statics/money.jpg" class="header-image absolute-top" />
    </q-header>

 <!-- <q-drawer
        v-model="leftDrawerOpen"
        show-if-above
        overlay
        :width="200"
        :breakpoint="400"
      >
        <q-scroll-area style="height: calc(100% - 150px); margin-top: 150px; border-right: 1px solid #ddd">
          <q-list padding>

            <q-item
              v-for="nav in navs"
              :key="nav.to"
              :to="nav.to"
              clickable 
              v-ripple
              exact>
              <q-item-section avatar>
                <q-icon :name="nav.icon" />
              </q-item-section>
              <q-item-section>
                <q-item-label> {{nav.label}} </q-item-label>
              </q-item-section>
            </q-item>

          </q-list>
        </q-scroll-area>

        <q-img class="absolute-top" src="statics/stats.jpg" style="height: 150px">
          <div class="absolute-bottom bg-transparent">
            <q-avatar size="56px" class="q-mb-sm">
              <img src="statics/avatar2.jpg">
            </q-avatar>
            <div class="text-weight-bold">{{nick}}</div>
          </div>
        </q-img>
      </q-drawer> -->

  <q-page-container>
      <div class="container-md">
      <router-view />
      </div>
    </q-page-container>

  </q-layout>
</template>

<script>

export default {
  name: "MainLayout",
  

  methods: {},

  mounted: function(){
    if (this.$store.getters['global/game'].active){
      this.$store.dispatch("global/leaveGame", this.products);
      window.location.reload()
    }
  },

  data() {
    return {
      leftDrawerOpen: false,
      // navs : [
      //   {
      //     label: 'Home',
      //     to: '/',
      //     icon: 'home',
      //   },
      //   {
      //     label: 'Create',
      //     to: '/create',
      //     icon: 'add',
          
      //   },
      //   {
      //     label: 'Join',
      //     to: '/join',
      //     icon: 'search',
          
      //   },
      //   {
      //     label: 'Game',
      //     to: '/game',
      //     icon: 'videogame_asset',
          
      //   }
      // ]
    };
  }
};
</script>

<style lang="scss">
.header-image {
  height: 100%;
  z-index: -1;
  opacity: 0.5;
  filter: grayscale(100%);
}
</style>
