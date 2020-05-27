/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import store from '@/store/index'
/**
 * @param {Array} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export const checkPerm = function(value) {
  if (value && value instanceof Array && value.length > 0) {
    const roles = store.getters && store.getters.permissions
    const permissionRoles = value

    const hasPermission = roles.some(role => {
      return permissionRoles.includes(role)
    })
    if (!hasPermission) {
      return false
    }
    return true
  } else {
    console.error(`need roles! Like v-if="checkPerm['op_editor']"`)
    return false
  }
}

export const checkUser = function(value) {
  const username = store.getters.userInfo.realName
  if (value === username) {
    return true
  }
  return false
}
