import { defineStore } from 'pinia'
import { ref } from 'vue'

type PersonName = {
  firstName?: string,
  lastName?: string
}
export const usePersonStore = defineStore('person', () => {
  const personName = ref<PersonName>({
    firstName: 'LeBron',
    lastName: 'James'
  })

  function setPersonName(personName1: PersonName) {
    personName.value = personName1
  }

  return { personName, setPersonName }
})